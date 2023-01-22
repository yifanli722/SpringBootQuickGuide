package com.raken.sendgridwrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.raken.sendgridwrapper.controller")
@ComponentScan("com.raken.sendgridwrapper.config")
public class SendGridWrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendGridWrapperApplication.class, args);
	}

}
