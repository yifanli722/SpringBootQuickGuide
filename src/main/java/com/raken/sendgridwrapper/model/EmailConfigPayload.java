package com.raken.sendgridwrapper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmailConfigPayload {
    private String To;
    private final Set<String> CC;
    private final Set<String> BCC;
    private final String Subject;
    private String Body;

    @JsonCreator
    public EmailConfigPayload(@JsonProperty("To") String To, @JsonProperty("CC") List<String> CC,
                              @JsonProperty("BCC") List<String> BCC, @JsonProperty("Subject") String Subject,
                              @JsonProperty("Body") String Body) {
        this.To = To;
        this.CC = new HashSet<>(CC);
        Set<String> temp = new HashSet<>(BCC);
        temp.removeAll(this.CC);
        this.BCC = temp;
        this.Subject = Subject;
        this.Body = Body;
    }

    public String filterNonRakenTargets() {
        StringBuilder builder = new StringBuilder();
        String rakenDomain = "rakenapp.com";
        this.CC.stream().filter(cc -> !cc.endsWith(rakenDomain)).forEach(cc -> {
            builder.append(cc).append(System.getProperty("line.separator"));
        });
        this.CC.removeIf(cc -> !cc.endsWith(rakenDomain));
        this.BCC.stream().filter(bcc -> !bcc.endsWith(rakenDomain)).forEach(bcc -> {
            builder.append(bcc).append(System.getProperty("line.separator"));
        });
        this.BCC.removeIf(bcc -> !bcc.endsWith(rakenDomain));
        if(!this.To.endsWith(rakenDomain))
            builder.append(this.To).append(System.getProperty("line.separator"));
        return builder.toString();
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public Set<String> getCC() {
        return CC;
    }

    public Set<String> getBCC() {
        return BCC;
    }

    public String getSubject() {
        return Subject;
    }

    public String getBody() {
        return Body.trim();
    }

    public void enrichBody(String text) {
        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode rootNode;
        Object[] lastNodes;
        try {
            rootNode = cleaner.clean(this.Body);
            lastNodes = rootNode.evaluateXPath("//*[last()]");
        } catch (Exception e) {
            return;
        }

        TagNode newPNode = new TagNode("p");
        newPNode.addChild(new ContentNode(text));
        ((TagNode) lastNodes[0]).addChild(newPNode);

        this.Body = cleaner.getInnerHtml(rootNode);
    }

    @Override
    public String toString() {
        return "EmailConfigPayload{" +
                "\n\tTo='" + To +
                "\n\tCC=" + CC +
                "\n\tBCC=" + BCC +
                "\n\tSubject='" + Subject +
                "\n\tBody='" + Body +
                "\n}";
    }
}

