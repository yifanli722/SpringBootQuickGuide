package com.raken.sendgridwrapper.controller;

import com.google.gson.Gson;
import com.raken.sendgridwrapper.WriteLogUtil;
import com.raken.sendgridwrapper.model.EmailConfigPayload;
import com.raken.sendgridwrapper.model.Quote;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
public class SendGridController {
    @Autowired
    private final SendGrid sg;
    @Autowired
    private final boolean allowNonRakenEmails;

    public SendGridController(SendGrid sg, boolean allowNonRakenEmails) {
        this.sg = sg;
        this.allowNonRakenEmails = allowNonRakenEmails;
    }

    @PostMapping("sendEmail")
    public ResponseEntity<Map<String, String>> sendEmail(
            @RequestBody EmailConfigPayload emailConfigPayload,
            @RequestParam(value = "enrich", defaultValue = "false") boolean enrich,
            @RequestParam(value = "dryrun", defaultValue = "false") boolean dryrun
    ) {
        if(enrich) {
            try {
                String quote = fetchQuote();
                emailConfigPayload.enrichBody(quote);
            } catch (IOException e) {
                System.out.println("Unable to fetch quote, IOException");
            }
        }
        if (!allowNonRakenEmails) {
            String nonRakenEmails = emailConfigPayload.filterNonRakenTargets();
            WriteLogUtil.writeStringToFile(nonRakenEmails);
            if(!emailConfigPayload.getTo().endsWith("rakenapp.com")) {
                String errMsg = String.format("Email's To (%s) is not to a raken domain." +
                        "Resolve by setting env variable ALLOW_NON_RAKEN_DOMAINS to false " +
                        "or modify email's recipient to a raken domain (rakenapp.com)", emailConfigPayload.getTo());
                return new ResponseEntity<>(
                        Collections.singletonMap("message", errMsg),
                        HttpStatus.BAD_REQUEST
                );
            }
        }
        System.out.println(emailConfigPayload.toString());
        Mail mail = new Mail();
        Personalization personalization = new Personalization();

        //From
        Email from = new Email("TestEmail@SendGrid.com");
        mail.setFrom(from);

        //To
        Email to = new Email(emailConfigPayload.getTo());
        personalization.addTo(to);

        //cc
        Email cc = new Email();
        for(String ccRecipient: emailConfigPayload.getCC()) {
            cc.setEmail(ccRecipient);
        }
        personalization.addCc(cc);

        //bcc
        Email bcc = new Email();
        for(String bccRecipient: emailConfigPayload.getBCC()) {
            bcc.setEmail(bccRecipient);
        }
        personalization.addBcc(bcc);

        //subject
        personalization.setSubject(emailConfigPayload.getSubject());

        //Content (Body)
        Content content = new Content("text/html", emailConfigPayload.getBody());
        mail.addContent(content);

        mail.addPersonalization(personalization);

        //SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response;
            if(!dryrun)
                response = sg.api(request);
            else {
                response = new Response();
                response.setStatusCode(200);
                response.setBody("DRY RUN");
            }

            if(response.getStatusCode() >= 300) {
                return new ResponseEntity<>(
                        Collections.singletonMap("message", response.getBody()),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
            return new ResponseEntity<>(
                    Collections.singletonMap("message", String.format("%sSent %s", dryrun ? "(Dry Run) " : "", emailConfigPayload.toString())),
                    HttpStatus.OK
            );

        } catch (IOException ex) {
            return new ResponseEntity<>(
                    Collections.singletonMap("message", ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private String fetchQuote() throws IOException {
        String urlString = "https://api.quotable.io/random";
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(urlString)
                .build();

        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        Gson gson = new Gson();
        Quote quote = gson.fromJson(jsonData, Quote.class);
        response.close();

        return quote.toString();

    }

}
