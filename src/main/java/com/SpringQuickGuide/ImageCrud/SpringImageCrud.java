package com.SpringQuickGuide.ImageCrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringImageCrud {
	public static void main(String[] args) {
		SpringApplication.run(SpringImageCrud.class, args);
	}
}

/*
 * Spring's annotations are used to mark classes to be handled by spring.
 * Classes marked with @Component or one of its subclasses (@Service, @Repository, @Controller) mark these classes to be
 * handled by dependency injection.
 */