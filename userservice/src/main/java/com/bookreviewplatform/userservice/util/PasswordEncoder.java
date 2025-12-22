package com.bookreviewplatform.userservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Utility class for password encoding and verification using BCrypt hashing algorithm.
 * 
 * <p>This class wraps Spring Security's BCryptPasswordEncoder to provide secure password
 * hashing with appropriate salt rounds. BCrypt is a one-way hashing function that is
 * intentionally slow to prevent brute-force attacks.</p>
 * 
 * <p><strong>Security Note:</strong> Never store plain text passwords. Always use this
 * encoder before persisting passwords to the database.</p>
 * 
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Component
public class PasswordEncoder {
    
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    /**
     * Constructs a PasswordEncoder with BCrypt strength of 10 rounds.
     * This provides a good balance between security and performance.
     */
    public PasswordEncoder() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
    }
    
    /**
     * Encodes a raw password using BCrypt hashing algorithm.
     * 
     * @param rawPassword The plain text password to encode
     * @return The BCrypt hashed password string
     * @throws IllegalArgumentException if rawPassword is null
     */
    public String encode(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("Raw password cannot be null");
        }
        return bCryptPasswordEncoder.encode(rawPassword);
    }
    
    /**
     * Verifies if a raw password matches an encoded password.
     * 
     * @param rawPassword The plain text password to verify
     * @param encodedPassword The BCrypt hashed password to compare against
     * @return true if the passwords match, false otherwise
     * @throws IllegalArgumentException if either parameter is null
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            throw new IllegalArgumentException("Passwords cannot be null");
        }
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
