package com.bookreviewplatform.userservice.service.custom;

import com.bookreviewplatform.userservice.dto.AuthResponse;
import com.bookreviewplatform.userservice.dto.LoginRequest;
import com.bookreviewplatform.userservice.dto.RegisterRequest;
import com.bookreviewplatform.userservice.entity.UserEntity;
import com.bookreviewplatform.userservice.enums.Role;
import com.bookreviewplatform.userservice.payloads.StandardResponse;
import com.bookreviewplatform.userservice.repository.UserRepository;
import com.bookreviewplatform.userservice.service.AuthService;
import com.bookreviewplatform.userservice.util.JwtUtil;
import com.bookreviewplatform.userservice.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of the AuthService interface.
 * 
 * <p>
 * This service handles all authentication-related business logic including
 * user registration, login, password encoding, and JWT token generation.
 * </p>
 * 
 * <p>
 * Security features:
 * - Passwords are hashed using BCrypt before storage
 * - Duplicate username/email validation
 * - Generic error messages to prevent information leakage
 * - JWT tokens for stateless authentication
 * </p>
 * 
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final Logger logger = Logger.getLogger(AuthServiceImpl.class.getName());
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;

        /**
         * Registers a new user in the system.
         * 
         * <p>
         * This method performs the following operations:
         * 1. Validates username uniqueness
         * 2. Validates email uniqueness
         * 3. Hashes the password using BCrypt
         * 4. Creates and saves the user entity
         * 5. Generates a JWT token for immediate authentication
         * 
         * @param request The registration request containing username, email, password,
         *                and roles
         * @return StandardResponse containing AuthResponse with token and user details
         *         on success,
         *         or error details on failure (409 for duplicates, 400 for validation
         *         errors)
         */
        @Override
        @Transactional
        public StandardResponse<AuthResponse> register(RegisterRequest request) {
                try {
                        logger.info("Processing registration request for username: " + request.getUsername());

                        // Validate required fields
                        if (request.getUsername() == null || request.getUsername().isBlank()) {
                                logger.warning("Registration failed: Username is required");
                                return StandardResponse.<AuthResponse>builder()
                                                .status(400)
                                                .success(false)
                                                .message("Validation failed")
                                                .error("Username is required")
                                                .timestamp(java.time.LocalDateTime.now())
                                                .build();
                        }

                        if (request.getEmail() == null || request.getEmail().isBlank()) {
                                logger.warning("Registration failed: Email is required");
                                return StandardResponse.<AuthResponse>builder()
                                                .status(400)
                                                .success(false)
                                                .message("Validation failed")
                                                .error("Email is required")
                                                .timestamp(java.time.LocalDateTime.now())
                                                .build();
                        }

                        if (request.getPassword() == null || request.getPassword().isBlank()) {
                                logger.warning("Registration failed: Password is required");
                                return StandardResponse.<AuthResponse>builder()
                                                .status(400)
                                                .success(false)
                                                .message("Validation failed")
                                                .error("Password is required")
                                                .timestamp(java.time.LocalDateTime.now())
                                                .build();
                        }

                        // Validate role
                        if (request.getRole() == null) {
                                logger.warning("Registration failed: Role is required");
                                return StandardResponse.<AuthResponse>builder()
                                                .status(400)
                                                .success(false)
                                                .message("Validation failed")
                                                .error("Role is required")
                                                .timestamp(java.time.LocalDateTime.now())
                                                .build();
                        }

                        Role role = request.getRole();

                        // Check for duplicate username
                        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                                String message = "Username already taken: " + request.getUsername();
                                logger.warning(message);
                                return StandardResponse.<AuthResponse>builder()
                                                .status(409)
                                                .success(false)
                                                .message("Duplicate resource")
                                                .error(message)
                                                .timestamp(java.time.LocalDateTime.now())
                                                .build();
                        }

                        // Check for duplicate email
                        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                                String message = "Email already registered: " + request.getEmail();
                                logger.warning(message);
                                return StandardResponse.<AuthResponse>builder()
                                                .status(409)
                                                .success(false)
                                                .message("Duplicate resource")
                                                .error(message)
                                                .timestamp(java.time.LocalDateTime.now())
                                                .build();
                        }

                        // Hash the password before storing
                        String hashedPassword = passwordEncoder.encode(request.getPassword());
                        logger.fine("Password hashed successfully for user: " + request.getUsername());

                        // Create user entity with single role
                        List<Role> rolesList = new ArrayList<>();
                        rolesList.add(role);

                        UserEntity userEntity = UserEntity.builder()
                                        .username(request.getUsername())
                                        .email(request.getEmail())
                                        .password(hashedPassword)
                                        .roles(rolesList)
                                        .build();

                        // Save user to database
                        UserEntity savedUser = userRepository.save(userEntity);
                        logger.info("User registered successfully with id: " + savedUser.getId() + " and role: "
                                        + role);

                        // Create response without token
                        AuthResponse authResponse = new AuthResponse(
                                        null,
                                        savedUser.getUsername(),
                                        role,
                                        "User registered successfully. Please login to get your token.");

                        return StandardResponse.<AuthResponse>builder()
                                        .status(201)
                                        .success(true)
                                        .message("Registration successful")
                                        .data(authResponse)
                                        .timestamp(java.time.LocalDateTime.now())
                                        .build();

                } catch (Exception e) {
                        logger.severe("Error during registration: " + e.getMessage());
                        return StandardResponse.<AuthResponse>builder()
                                        .status(500)
                                        .success(false)
                                        .message("Registration failed")
                                        .error(e.getMessage())
                                        .timestamp(java.time.LocalDateTime.now())
                                        .build();
                }
        }

        /**
         * Authenticates a user with username and password.
         * 
         * <p>
         * This method performs the following operations:
         * 1. Finds the user by username
         * 2. Verifies the password using BCrypt
         * 3. Generates a JWT token if authentication is successful
         * 
         * <p>
         * Security note: Returns generic error messages to prevent username enumeration
         * attacks.
         * 
         * @param request The login request containing username and password
         * @return StandardResponse containing AuthResponse with token and user details
         *         on success,
         *         or error details on failure (401 for invalid credentials)
         */
        @Override
        public StandardResponse<AuthResponse> login(LoginRequest request) {
                try {
                        logger.info("Processing login request for email: " + request.getEmail());

                        // Validate required fields
                        if (request.getEmail() == null || request.getEmail().isBlank()) {
                                logger.warning("Login failed: Email is required");
                                return StandardResponse.<AuthResponse>builder()
                                                .status(400)
                                                .success(false)
                                                .message("Validation failed")
                                                .error("Email is required")
                                                .timestamp(java.time.LocalDateTime.now())
                                                .build();
                        }

                        if (request.getPassword() == null || request.getPassword().isBlank()) {
                                logger.warning("Login failed: Password is required");
                                return StandardResponse.<AuthResponse>builder()
                                                .status(400)
                                                .success(false)
                                                .message("Validation failed")
                                                .error("Password is required")
                                                .timestamp(java.time.LocalDateTime.now())
                                                .build();
                        }

                        // Find user by email
                        UserEntity user = userRepository.findByEmail(request.getEmail())
                                        .orElse(null);

                        // If user not found or password doesn't match, return generic error
                        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                                logger.warning("Login failed: Invalid credentials for email: "
                                                + request.getEmail());
                                return StandardResponse.<AuthResponse>builder()
                                                .status(401)
                                                .success(false)
                                                .message("Authentication failed")
                                                .error("Invalid credentials")
                                                .timestamp(java.time.LocalDateTime.now())
                                                .build();
                        }

                        // Get user role (first role from the list)
                        Role userRole = user.getRoles() != null && !user.getRoles().isEmpty()
                                        ? user.getRoles().get(0)
                                        : Role.USER;

                        // Generate JWT token with role
                        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), userRole.name());
                        logger.info("User logged in successfully: " + user.getUsername() + " with role: " + userRole);

                        // Create response with token and role
                        AuthResponse authResponse = new AuthResponse(
                                        token,
                                        user.getUsername(),
                                        userRole,
                                        "Login successful");

                        return StandardResponse.<AuthResponse>builder()
                                        .status(200)
                                        .success(true)
                                        .message("Authentication successful")
                                        .data(authResponse)
                                        .timestamp(java.time.LocalDateTime.now())
                                        .build();

                } catch (Exception e) {
                        logger.severe("Error during login: " + e.getMessage());
                        return StandardResponse.<AuthResponse>builder()
                                        .status(500)
                                        .success(false)
                                        .message("Login failed")
                                        .error(e.getMessage())
                                        .timestamp(java.time.LocalDateTime.now())
                                        .build();
                }
        }
}
