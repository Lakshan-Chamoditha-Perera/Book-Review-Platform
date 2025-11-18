package com.bookreviewplatform.apigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.logging.Logger;

@SpringBootApplication
public class ApigatewayserviceApplication {

	private static final Logger logger = Logger.getLogger(ApigatewayserviceApplication.class.getName());

	public static void main(String[] args) {
		logger.info("Starting API Gateway Service...");
		SpringApplication.run(ApigatewayserviceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		logger.info("API Gateway Service started successfully and is ready to route requests");
		logger.info("Gateway is listening on port 9000");
		logger.info("Configured routes: userservice, bookservice, reviewservice");
	}

}
