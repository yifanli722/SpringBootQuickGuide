package com.raken.sendgridwrapper.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EmailConfigPayloadTest {

    @Test
    void filterNonRakenTargets() {
        EmailConfigPayload emailConfigPayload = new EmailConfigPayload("test@rakenapp.com",
                Arrays.asList("cc1@rakenapp.com", "cc2@example.com"),
                Arrays.asList("bcc1@example.com", "bcc2@rakenapp.com"),
                "Test Subject",
                "<h1>Test Body</h1>");
        String nonRakenEmails = emailConfigPayload.filterNonRakenTargets();

        assertTrue(nonRakenEmails.contains("cc2@example.com"));
        assertTrue(nonRakenEmails.contains("bcc1@example.com"));

        assertTrue(emailConfigPayload.getCC().stream().allMatch(cc -> cc.endsWith("@rakenapp.com")));
    }

    @Test
    void enrichBody() {
        EmailConfigPayload emailConfigPayload = new EmailConfigPayload("test@example.com",
                Arrays.asList("cc1@example.com", "cc2@example.com"),
                Arrays.asList("bcc1@example.com", "bcc2@example.com"),
                "Test Subject",
                "<h1>Test Body</h1>");

        emailConfigPayload.enrichBody("Hello World");
        String body = emailConfigPayload.getBody();
        assertEquals("<head />\n" +
                "<body><h1>Test Body</h1><p>Hello World</p></body>", body);
    }

    @Test
    void testToString() {
        EmailConfigPayload emailConfigPayload = new EmailConfigPayload("test@rakenapp.com",
                Arrays.asList("cc1@rakenapp.com", "cc2@example.com"),
                Arrays.asList("bcc1@example.com", "bcc2@rakenapp.com"),
                "Test Subject",
                "<h1>Test Body</h1>");

        assertEquals("EmailConfigPayload{\n" +
                "\tTo='test@rakenapp.com\n" +
                "\tCC=[cc1@rakenapp.com, cc2@example.com]\n" +
                "\tBCC=[bcc2@rakenapp.com, bcc1@example.com]\n" +
                "\tSubject='Test Subject\n" +
                "\tBody='<h1>Test Body</h1>\n" +
                "}", emailConfigPayload.toString());
    }
}