package com.bookreviewplatform.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Represents a user in the Book Review Platform system.
 *
 * <p>This entity is mapped to the "users" table (or default naming) in the database
 * and holds core user information required for authentication and identification.</p>
 *
 * <p><strong>Security Note:</strong> The {@code password} field stores a hashed password
 * (never plain text). Passwords must be encoded using a strong one-way hashing algorithm
 * (e.g., BCrypt) before persisting.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password") // Critical: Prevent accidental logging of password
public class UserEntity {

    /**
     * Unique identifier for the user.
     * Uses UUID (Universally Unique Identifier) for distributed system safety
     * and to avoid sequential ID exposure.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    /**
     * Unique username chosen by the user.
     * Must be unique across all users and cannot be null.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Hashed user password.
     *
     * <p><strong>Never store plain-text passwords.</strong>
     * This field should contain the output of a secure password encoder
     * (e.g., BCryptPasswordEncoder in Spring Security).</p>
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Unique email address of the user.
     * Used for login (optional), notifications, and password recovery.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;
}