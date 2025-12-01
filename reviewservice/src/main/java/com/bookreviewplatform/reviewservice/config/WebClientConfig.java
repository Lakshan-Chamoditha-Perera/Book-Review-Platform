package com.bookreviewplatform.reviewservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for setting up {@link WebClient} instances used by the Review Service
 * to communicate with other microservices (Book Service & User Service).
 *
 * <p>Provides load-balanced, reusable, and type-safe HTTP clients with pre-configured base URLs.</p>
 *
 * <p>Uses Spring Cloud LoadBalancer (via {@code @LoadBalanced}) to resolve service names
 * (e.g., {@code bookservice}, {@code userservice}) through the service registry (Eureka/Nacos/Consul),
 * enabling resilient inter-service communication in a microservices architecture.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Configuration
public class WebClientConfig {

    /**
     * Provides a {@link WebClient.Builder} marked with {@code @LoadBalanced}.
     *
     * <p>This builder automatically integrates with Spring Cloud LoadBalancer,
     * allowing the use of service names (e.g., "http://bookservice") instead of hard-coded host:port.</p>
     *
     * <p>Marked as {@code @Bean} so it can be injected and customized further if needed.</p>
     *
     * @return a load-balanced {@link WebClient.Builder} instance
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /**
     * Creates a pre-configured {@link WebClient} for communicating with the <strong>Book Service</strong>.
     *
     * <p>Base URL: {@code http://bookservice/api/v1/books}</p>
     * <p>Used for validating book existence, fetching book details, etc.</p>
     *
     * @param webClientBuilder the load-balanced builder
     * @return configured {@link WebClient} for Book Service
     */
    @Bean
    public WebClient bookWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("http://bookservice/api/v1/books")
                .build();
    }

    /**
     * Creates a pre-configured {@link WebClient} for communicating with the <strong>User Service</strong>.
     *
     * <p>Base URL: {@code http://userservice/api/v1/users}</p>
     * <p>Used for validating user existence, retrieving user profile info, etc.</p>
     *
     * @param webClientBuilder the load-balanced builder
     * @return configured {@link WebClient} User Service
     */
    @Bean
    public WebClient userWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("http://userservice/api/v1/users")
                .build();
    }
}