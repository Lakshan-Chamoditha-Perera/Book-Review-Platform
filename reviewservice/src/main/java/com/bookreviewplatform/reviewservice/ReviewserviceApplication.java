package com.bookreviewplatform.reviewservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.logging.Logger;

@SpringBootApplication
public class ReviewserviceApplication {

	private static final Logger logger = Logger.getLogger(ReviewserviceApplication.class.getName());

	public static void main(String[] args) {
		logger.info("Starting Review Service Application...");
		SpringApplication.run(ReviewserviceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		logger.info("Review Service Application started successfully and is ready to accept requests");
	}

}
