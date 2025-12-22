package com.bookreviewplatform.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
                return builder.routes()
                                // Authentication Routes
                                .route("auth", r -> r
                                                .path("/api/v1/auth/**")
                                                .uri("lb://userservice"))

                                // User Service Routes
                                .route("userservice", r -> r
                                                .path("/api/v1/users/**")
                                                .uri("lb://userservice"))

                                // Book Service Routes
                                .route("bookservice", r -> r
                                                .path("/api/v1/books/**")
                                                .uri("lb://bookservice"))

                                // Review Service Routes
                                .route("reviewservice", r -> r
                                                .path("/api/v1/reviews/**")
                                                .uri("lb://reviewservice"))

                                .build();
        }
}