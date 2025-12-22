package com.bookreviewplatform.userservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

 /**
 * Utility class for password encoding and verification using BCrypt hashing
 * algorithm.
 * 
 * <p>
 * This class wraps Spring Security's BCryptPasswordEncoder to provide secure
 * password
 * hashing with automatic salt generation. BCrypt is a one-way hashing function
 * that is
 * intentionally slow to prevent brute-force attacks.
 * </p>
 * 
 * <p>
 * <strong>How BCrypt Salting Works:</strong>
 * </p>
 * <ul>
 * <li>Each password gets a unique, randomly-generated salt</li>
 * <li>The salt is automatically embedded in the hash string</li>
 * <li>No separate salt field is needed in the database</li>
 * <li>The same password will produce different hashes due to unique salts</li>
 * </ul>
 * 
 * <p>
 * <strong>BCrypt Hash Format:</strong><br>
 * $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy<br>
 * └─┬─┘└┬┘└──────────┬──────────┘└──────────┬──────────┘<br>
 * &nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;│<br>
 * &nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─
 * Hash (31 chars)<br>
 * &nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─
 * Salt (22 chars)<br>
 * &nbsp;&nbsp;│&nbsp;&nbsp;&nbsp;└─ Cost factor (10 = 2^10 rounds)<br>
 * &nbsp;&nbsp;└─ Algorithm version ($2a, $2b, or $2y)<br>
 * </p>
 * 
 * <p>
 * <strong>Security Note:</strong> Never store plain text passwords. Always use
 * this encoder before persisting passwords to the database.
 * </p>
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
     * Encodes a raw password using BCrypt hashing algorithm with automatic salt
     * generation.
     * 
     * <p>
     * BCrypt automatically generates a unique random salt for each password and
     * embeds
     * it in the resulting hash. This means the same password will produce different
     * hashes
     * each time this method is called, which is the desired behavior for security.
     * </p>
     * 
     * <p>
     * <strong>Example:</strong><br>
     * encode("mypassword") → "$2a$10$abc...xyz" (60 characters)<br>
     * encode("mypassword") → "$2a$10$def...uvw" (different hash, different salt)
     * </p>
     * 
     * @param rawPassword The plain text password to encode
     * @return The BCrypt hashed password string (60 characters) with embedded salt
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
     * <p>
     * This method extracts the salt from the encoded password hash and uses it
     * to hash the raw password, then compares the results. This is how BCrypt
     * verifies passwords without storing the salt separately.
     * </p>
     * 
     * <p>
     * <strong>Example:</strong><br>
     * matches("mypassword", "$2a$10$abc...xyz") → true (if hash was created from
     * "mypassword")<br>
     * matches("wrongpass", "$2a$10$abc...xyz") → false
     * </p>
     * 
     * @param rawPassword     The plain text password to verify
     * @param encodedPassword The BCrypt hashed password to compare against
     *                        (contains embedded salt)
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
