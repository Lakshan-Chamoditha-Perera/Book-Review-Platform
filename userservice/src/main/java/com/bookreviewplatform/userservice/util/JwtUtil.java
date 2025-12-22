package com.bookreviewplatform.userservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Utility class for JWT (JSON Web Token) operations including token generation,
 * validation, and claim extraction.
 * 
 * <p>
 * This class handles all JWT-related operations for the authentication system,
 * using the JJWT library with HS256 algorithm for token signing.
 * </p>
 * 
 * <p>
 * <strong>Security Note:</strong> The JWT secret key must be at least 256 bits
 * and should be stored securely (environment variables or secret management
 * system).
 * </p>
 * 
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration; // 24 hours in milliseconds

    @PostConstruct
    public void validateJwtConfiguration() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);

            if (keyBytes.length < 32) {
                throw new IllegalStateException(
                        "JWT secret must be at least 256 bits (32 bytes) after BASE64 decoding. " +
                                "Current length: " + keyBytes.length + " bytes");
            }

            // Check if using default/weak secret
            if (secret.contains("your-256-bit-secret") || secret.contains("change-in-production")) {
                logger.warn("WARNING: Using default JWT secret! Change this in production for security.");
            }

            logger.info("JWT configuration validated successfully. Secret key length: {} bytes", keyBytes.length);

        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                    "Invalid JWT secret: Must be a valid BASE64-encoded string. Error: " + e.getMessage(), e);
        }
    }

    /**
     * Generates a JWT token with username, user ID, and role claims.
     * 
     * @param username The username to include in the token
     * @param userId   The user ID to include in the token
     * @param role     The user role to include in the token (ADMIN or USER)
     * @return A signed JWT token string
     * @throws IllegalArgumentException if username, userId, or role is null
     */
    public String generateToken(String username, UUID userId, String role) {
        if (username == null || userId == null || role == null) {
            throw new IllegalArgumentException("Username, userId, and role cannot be null");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("userId", userId.toString());
        claims.put("role", role);

        return createToken(claims, username);
    }

    /**
     * Creates a JWT token with the specified claims and subject.
     * 
     * @param claims  The claims to include in the token
     * @param subject The subject (username) of the token
     * @return A signed JWT token string
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validates a JWT token by checking its signature and expiration.
     * 
     * @param token The JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the username from a JWT token.
     * 
     * @param token The JWT token
     * @return The username claim from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, "username", String.class);
    }

    /**
     * Extracts the user ID from a JWT token.
     * 
     * @param token The JWT token
     * @return The user ID claim from the token
     */
    public UUID extractUserId(String token) {
        String userIdStr = extractClaim(token, "userId", String.class);
        return UUID.fromString(userIdStr);
    }

    /**
     * Extracts the role from a JWT token.
     * 
     * @param token The JWT token
     * @return The role claim from the token
     */
    public String extractRole(String token) {
        return extractClaim(token, "role", String.class);
    }

    /**
     * Extracts a specific claim from a JWT token.
     * 
     * @param token     The JWT token
     * @param claimName The name of the claim to extract
     * @param claimType The type of the claim
     * @param <T>       The type parameter
     * @return The claim value
     */
    private <T> T extractClaim(String token, String claimName, Class<T> claimType) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimName, claimType);
    }

    /**
     * Extracts all claims from a JWT token.
     * 
     * @param token The JWT token
     * @return The claims from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Gets the signing key for JWT operations.
     * 
     * @return The SecretKey for signing/verifying tokens
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
