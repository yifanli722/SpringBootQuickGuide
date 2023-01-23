package com.raken.sendgridwrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
@ComponentScan("com.raken.sendgridwrapper.controller")
@ComponentScan("com.raken.sendgridwrapper.config")
public class SendGridWrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendGridWrapperApplication.class, args);
	}

	public static void writeStringToFile(String filePath, String content) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
