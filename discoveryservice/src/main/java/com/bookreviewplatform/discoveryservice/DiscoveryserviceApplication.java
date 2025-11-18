package com.bookreviewplatform.discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.event.EventListener;

import java.util.logging.Logger;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryserviceApplication {

	private static final Logger logger = Logger.getLogger(DiscoveryserviceApplication.class.getName());

	public static void main(String[] args) {
		logger.info("Starting Discovery Service (Eureka Server)...");
		SpringApplication.run(DiscoveryserviceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		logger.info("Discovery Service started successfully - Eureka Server is ready to register services");
	}

}
