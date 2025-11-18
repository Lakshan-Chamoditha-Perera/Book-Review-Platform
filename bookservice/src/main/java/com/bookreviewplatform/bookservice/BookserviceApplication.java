package com.bookreviewplatform.bookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.logging.Logger;

@SpringBootApplication
public class BookserviceApplication {

	private static final Logger logger = Logger.getLogger(BookserviceApplication.class.getName());

	public static void main(String[] args) {
		logger.info("Starting Book Service Application...");
		SpringApplication.run(BookserviceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		logger.info("Book Service Application started successfully and is ready to accept requests");
	}

}
