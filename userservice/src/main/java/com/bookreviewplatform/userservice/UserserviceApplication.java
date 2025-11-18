package com.bookreviewplatform.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.logging.Logger;

@SpringBootApplication
public class UserserviceApplication {

	private static final Logger logger = Logger.getLogger(UserserviceApplication.class.getName());

	public static void main(String[] args) {
		logger.info("Starting User Service Application...");
		SpringApplication.run(UserserviceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		logger.info("User Service Application started successfully and is ready to accept requests");
	}

}
