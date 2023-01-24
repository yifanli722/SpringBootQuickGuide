package com.raken.sendgridwrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raken.sendgridwrapper.model.EmailConfigPayload;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SendGridControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SendGrid sendGridMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendEmailEndpointWithSuccess() throws Exception {
        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);
        mockResponse.setBody("{\"message\":\"Email sent successfully\"}");
        mockResponse.setHeaders(new HashMap<>());

        when(sendGridMock.api(any(Request.class))).thenReturn(mockResponse);

        EmailConfigPayload emailConfigPayload = new EmailConfigPayload("test@rakenapp.com",
                Arrays.asList("cc1@example.com", "cc2@example.com"),
                Arrays.asList("bcc1@example.com", "bcc2@example.com"),
                "Test Subject",
                "<h1>Test Body</h1>");

        MvcResult result = mockMvc.perform(post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"To\": \"yifanli722@rakenapp.com\",\n" +
                                "    \"CC\": [\"test@gmail.com\", \"test2@gmail.com\"],\n" +
                                "    \"BCC\": [\"test3@rakenapp.com\"],\n" +
                                "    \"Subject\": \"Lorem Ipsum\",\n" +
                                "    \"Body\": \"<!DOCTYPE html> <html> <head> <title>Lorem Ipsum</title> </head> <body> <h1>Lorem Ipsum</h1> <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, leo eget bibendum congue, nibh ipsum gravida velit, vel blandit nibh nibh non orci.</p> <p>Sed auctor, velit id pellentesque tempus, magna libero convallis velit, vel euismod augue velit vel velit.</p> </body> </html>\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Sent EmailConfigPayload{\n" +
                        "\tTo='yifanli722@rakenapp.com\n" +
                        "\tCC=[]\n" +
                        "\tBCC=[test3@rakenapp.com]\n" +
                        "\tSubject='Lorem Ipsum\n" +
                        "\tBody='<!DOCTYPE html> <html> <head> <title>Lorem Ipsum</title> </head> <body> <h1>Lorem Ipsum</h1> <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, leo eget bibendum congue, nibh ipsum gravida velit, vel blandit nibh nibh non orci.</p> <p>Sed auctor, velit id pellentesque tempus, magna libero convallis velit, vel euismod augue velit vel velit.</p> </body> </html>\n" +
                        "}",
                objectMapper.readTree(result.getResponse().getContentAsString()).get("message").asText());
    }
}
