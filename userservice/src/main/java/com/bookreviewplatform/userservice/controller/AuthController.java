package com.bookreviewplatform.userservice.controller;

import com.bookreviewplatform.userservice.dto.LoginRequest;
import com.bookreviewplatform.userservice.dto.RegisterRequest;
import com.bookreviewplatform.userservice.payloads.StandardResponse;
import com.bookreviewplatform.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * REST controller for authentication operations in the book review platform.
 *
 * <p>
 * This controller handles user registration and login operations,
 * delegating business logic to the AuthService. All endpoints are prefixed
 * with "/api/v1/auth".
 * </p>
 *
 * <p>
 * Endpoints:
 * - POST /api/v1/auth/register - Register a new user
 * - POST /api/v1/auth/login - Authenticate a user
 *
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Logger logger = Logger.getLogger(AuthController.class.getName());
    private final AuthService authService;

    /**
     * Registers a new user in the system.
     *
     * @param request The registration request containing user details (username,
     *                email, password, roles).
     * @return A {@link ResponseEntity} containing a {@link StandardResponse} with
     * the authentication details.
     * HTTP status: 201 Created.
     */
    @PostMapping("/register")
    public ResponseEntity<StandardResponse> register(@RequestBody RegisterRequest request) {
        logger.info("Received registration request for username: " + request.getUsername());
        return ResponseEntity.ok()
                .body(authService.register(request));
    }

    /**
     * Authenticates a user with email and password.
     *
     * @param request The login request containing email and password.
     * @return A {@link ResponseEntity} containing a {@link StandardResponse} with
     * the authentication details.
     * HTTP status: 200 OK.
     */
    @PostMapping("/login")
    public ResponseEntity<StandardResponse> login(@RequestBody LoginRequest request) {
        logger.info("Received login request for email: " + request.getEmail());
        return ResponseEntity.ok(authService.login(request));
    }
}
