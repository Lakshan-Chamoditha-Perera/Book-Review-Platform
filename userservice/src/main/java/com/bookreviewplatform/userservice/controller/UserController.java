package com.bookreviewplatform.userservice.controller;

import com.bookreviewplatform.userservice.dto.UserRequestDTO;
import com.bookreviewplatform.userservice.payloads.StandardResponse;
import com.bookreviewplatform.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * REST controller for managing user-related operations in the book review platform.
 * This controller handles CRUD operations for users, such as retrieving, creating, and deleting users.
 * All endpoints are prefixed with "/api/v1/users".
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UserService userService;

    /**
     * Retrieves all users in the system.
     *
     * @return A {@link ResponseEntity} containing a {@link StandardResponse} with the list of users.
     *         HTTP status: 200 OK.
     */
    @GetMapping()
    public ResponseEntity<StandardResponse> getAllUsers() {
        logger.info("Received request to get all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Creates a new user based on the provided request data.
     *
     * @param userRequestDTO The DTO containing user details (e.g., email, etc.).
     * @return A {@link ResponseEntity} containing a {@link StandardResponse} with the created user details.
     *         HTTP status: 201 Created.
     */
    @PostMapping()
    public ResponseEntity<StandardResponse> saveUser(@RequestBody UserRequestDTO userRequestDTO) {
        logger.info("Received request to create user with email: " + userRequestDTO.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(userRequestDTO));
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id The UUID of the user to delete.
     * @return A {@link ResponseEntity} containing a {@link StandardResponse} confirming deletion.
     *         HTTP status: 200 OK.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteUser(@PathVariable UUID id) {
        logger.info("Received request to delete user with id: " + id);
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user to retrieve.
     * @return A {@link ResponseEntity} containing a {@link StandardResponse} with the user details.
     *         HTTP status: 200 OK.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<StandardResponse> getUserByEmail(@PathVariable String email) {
        logger.info("Received request to get user by email: " + email);
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id The UUID of the user to retrieve.
     * @return A {@link ResponseEntity} containing a {@link StandardResponse} with the user details.
     *         HTTP status: 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getUserById(@PathVariable UUID id) {
        logger.info("Received request to get user by id: " + id);
        return ResponseEntity.ok(userService.getUserById(id));
    }
}