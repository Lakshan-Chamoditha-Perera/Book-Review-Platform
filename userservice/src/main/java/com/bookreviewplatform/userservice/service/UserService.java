package com.bookreviewplatform.userservice.service;

import com.bookreviewplatform.userservice.dto.UserRequestDTO;
import com.bookreviewplatform.userservice.payloads.StandardResponse;

import java.util.UUID;

/**
 * Service layer interface for managing user-related business operations
 * in the Book Review Platform.
 *
 * <p>Defines the contract for user CRUD operations and lookup methods.
 * All implementing classes must return responses wrapped in {@link StandardResponse}
 * to ensure consistent API formatting across the application.</p>
 *
 * <p>This interface is typically implemented by {@code UserServiceImpl} and
 * autowired into controllers or other services.</p>
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
public interface UserService {

    /**
     * Retrieves all registered users in the system.
     *
     * @return {@link StandardResponse} containing a list of user data (usually DTOs)
     * with HTTP-friendly structure and success flag
     */
    StandardResponse getAllUsers();

    /**
     * Creates a new user based on the provided registration data.
     *
     * <p>Validates input, checks for duplicate username/email,
     * hashes the password, and persists the user entity.</p>
     *
     * @param userRequestDTO DTO containing user registration details
     *                       (e.g., username, email, password)
     * @return {@link StandardResponse} with the created user details and success message
     * (typically returns HTTP 201 Created via controller)
     */
    StandardResponse saveUser(UserRequestDTO userRequestDTO);

    /**
     * Deletes a user permanently by their unique ID.
     *
     * @param id the {@link UUID} of the user to delete
     * @return {@link StandardResponse} confirming deletion or reporting error
     * (e.g., user not found)
     */
    StandardResponse deleteUser(UUID id);

    /**
     * Finds a user by their email address.
     *
     * <p>Useful for login, password reset password, and profile lookup.</p>
     *
     * @param email the email address of the user (case-sensitive)
     * @return {@link StandardResponse} containing the user data if found,
     * or an error if no user exists with that email
     */
    StandardResponse getUserByEmail(String email);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the {@link UUID} of the user
     * @return {@link StandardResponse} with user details if found,
     * or error response if user does not exist
     */
    StandardResponse getUserById(UUID id);
}