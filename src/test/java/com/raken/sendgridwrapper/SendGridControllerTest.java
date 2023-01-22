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

        EmailConfigPayload emailConfigPayload = new EmailConfigPayload("test@example.com",
                Arrays.asList("cc1@example.com", "cc2@example.com"),
                Arrays.asList("bcc1@example.com", "bcc2@example.com"),
                "Test Subject",
                "<h1>Test Body</h1>");

        MvcResult result = mockMvc.perform(post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailConfigPayload)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Sent EmailConfigPayload{To='test@example.com', CC=[cc1@example.com, cc2@example.com], BCC=[bcc1@example.com, bcc2@example.com], Subject='Test Subject', Body='<h1>Test Body</h1>'}",
                objectMapper.readTree(result.getResponse().getContentAsString()).get("message").asText());
    }
}
