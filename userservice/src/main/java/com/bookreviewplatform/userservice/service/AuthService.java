package com.bookreviewplatform.userservice.service;

import com.bookreviewplatform.userservice.dto.AuthResponse;
import com.bookreviewplatform.userservice.dto.LoginRequest;
import com.bookreviewplatform.userservice.dto.RegisterRequest;
import com.bookreviewplatform.userservice.payloads.StandardResponse;

/**
 * Service interface for authentication operations.
 * 
 * <p>This interface defines the contract for user authentication functionality
 * including registration and login operations. Implementations should handle
 * password encoding, JWT token generation, and validation logic.</p>
 * 
 * @author Lakshan Chamoditha Perera
 * @since 1.0
 */
public interface AuthService {
    
    /**
     * Registers a new user in the system.
     * 
     * <p>This method validates the registration request, checks for duplicate
     * username/email, hashes the password, creates the user account, and
     * generates a JWT token for immediate authentication.</p>
     * 
     * @param request The registration request containing username, email, password, and roles
     * @return StandardResponse containing AuthResponse with token and user details on success,
     *         or error details on failure
     */
    StandardResponse<AuthResponse> register(RegisterRequest request);
    
    /**
     * Authenticates a user with username and password.
     * 
     * <p>This method validates the login credentials, verifies the password,
     * and generates a JWT token if authentication is successful.</p>
     * 
     * @param request The login request containing username and password
     * @return StandardResponse containing AuthResponse with token and user details on success,
     *         or error details on failure
     */
    StandardResponse<AuthResponse> login(LoginRequest request);
}
