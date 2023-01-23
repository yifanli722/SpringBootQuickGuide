package com.raken.sendgridwrapper.config;

import com.sendgrid.SendGrid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(System.getenv("SENDGRID_API_KEY"));
    }

    @Bean
    public boolean allowNonRakenEmails() {
        return Boolean.parseBoolean(System.getenv("ALLOW_NON_RAKEN_DOMAINS"));
    }
}
