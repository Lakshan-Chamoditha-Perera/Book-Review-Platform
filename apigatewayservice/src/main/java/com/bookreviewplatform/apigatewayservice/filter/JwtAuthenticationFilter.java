package com.bookreviewplatform.apigatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.List;

/**
 * Global filter for JWT authentication in API Gateway.
 * Validates JWT tokens for protected routes.
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${api.public.endpoints}")
    private String publicEndpoints;

    @PostConstruct
    public void validateJwtConfiguration() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

            if (keyBytes.length < 32) {
                throw new IllegalStateException(
                        "JWT secret must be at least 256 bits (32 bytes) after BASE64 decoding. " +
                                "Current length: " + keyBytes.length + " bytes");
            }

            logger.info("JWT configuration validated successfully. Secret key length: {} bytes", keyBytes.length);

        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                    "Invalid JWT secret: Must be a valid BASE64-encoded string. Error: " + e.getMessage(), e);
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // Check if path is public
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // Extract JWT token from Authorization header
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            // Validate JWT token
            validateToken(token);

            // Token is valid, add user info to headers for downstream services
            Claims claims = extractClaims(token);
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", claims.get("userId", String.class))
                    .header("X-Username", claims.get("username", String.class))
                    .header("X-User-Role", claims.get("role", String.class))
                    .headers(headers -> headers.remove(HttpHeaders.AUTHORIZATION))
                    .build();

            logger.debug("JWT validation successful for user: {}", claims.get("username", String.class));
            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token: {}", e.getMessage());
            return onError(exchange, "Expired JWT token", HttpStatus.UNAUTHORIZED);
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            logger.warn("Malformed JWT token: {}", e.getMessage());
            return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            logger.warn("Unsupported JWT token: {}", e.getMessage());
            return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            logger.warn("JWT claims string is empty: {}", e.getMessage());
            return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("JWT token validation failed: {}", e.getMessage(), e);
            return onError(exchange, "JWT token validation failed", HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isPublicPath(String path) {
        List<String> publicPaths = Arrays.asList(publicEndpoints.split(","));
        return publicPaths.stream().anyMatch(pattern -> pathMatches(path, pattern));
    }

    private boolean pathMatches(String path, String pattern) {
        // Simple pattern matching (supports /** wildcard)
        if (pattern.endsWith("/**")) {
            String prefix = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(prefix);
        }
        return path.equals(pattern);
    }

    private void validateToken(String token) {
        Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        String errorResponse = String.format("{\"status\":%d,\"success\":false,\"message\":\"%s\",\"error\":\"%s\"}",
                status.value(), status.getReasonPhrase(), message);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.getBytes())));
    }

    @Override
    public int getOrder() {
        return -100; // High priority
    }
}
