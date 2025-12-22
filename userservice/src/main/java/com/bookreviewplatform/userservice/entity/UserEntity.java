package com.bookreviewplatform.userservice.entity;

import com.bookreviewplatform.userservice.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user in the Book Review Platform system.
 *
 * <p>
 * This entity is mapped to the "users" table (or default naming) in the
 * database
 * and holds core user information required for authentication and
 * identification.
 * </p>
 *
 * <p>
 * <strong>Security Note:</strong> The {@code password} field stores a hashed
 * password
 * (never plain text). Passwords must be encoded using a strong one-way hashing
 * algorithm
 * (e.g., BCrypt) before persisting.
 * </p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password")
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
     * Hashed user password with embedded salt.
     *
     * <p>
     * <strong>Never store plain-text passwords.</strong>
     * This field contains a BCrypt hash which includes:
     * - Algorithm version ($2a$, $2b$, or $2y$)
     * - Cost factor (number of hashing rounds, default: 10)
     * - 22-character salt (randomly generated per password)
     * - 31-character hash of the password
     * </p>
     * 
     * <p>
     * <strong>Example BCrypt hash format:</strong><br>
     * $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
     * </p>
     * 
     * <p>
     * The salt is automatically generated and embedded in the hash by BCrypt,
     * so no separate salt field is needed. Each password gets a unique salt.
     * </p>
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * Unique email address of the user.
     * Used for login (optional), notifications, and password recovery.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * User roles for authorization.
     * Stored as enum values in the database.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<Role> roles = new ArrayList<>();
}