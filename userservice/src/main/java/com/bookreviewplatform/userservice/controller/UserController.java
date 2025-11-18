package com.bookreviewplatform.userservice.controller;

import com.bookreviewplatform.userservice.payloads.StandardResponse;
import com.bookreviewplatform.userservice.dto.UserDTO;
import com.bookreviewplatform.userservice.dto.UserRequestDTO;
import com.bookreviewplatform.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<StandardResponse<List<UserDTO>>> getAllUsers() {
        logger.info("Received request to get all users");
        try {
            List<UserDTO> users = userService.getAllUsers();
            logger.info("Successfully retrieved " + users.size() + " users");
            return ResponseEntity.ok(StandardResponse.success("Users retrieved successfully", users));
        } catch (Exception e) {
            logger.severe("Failed to retrieve users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve users", e.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<StandardResponse<UserDTO>> saveUser(@RequestBody UserRequestDTO userRequestDTO) {
        logger.info("Received request to create user with email: " + userRequestDTO.getEmail());
        try {
            UserDTO savedUser = userService.saveUser(userRequestDTO);
            logger.info("Successfully created user with id: " + savedUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(StandardResponse.success("User created successfully", savedUser));
        } catch (Exception e) {
            logger.severe("Failed to create user with email " + userRequestDTO.getEmail() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.error("Failed to create user", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<Boolean>> deleteUser(@PathVariable UUID id) {
        logger.info("Received request to delete user with id: " + id);
        try {
            Boolean deleted = userService.deleteUser(id);
            if (deleted) {
                logger.info("Successfully deleted user with id: " + id);
                return ResponseEntity.ok(StandardResponse.success("User deleted successfully", true));
            } else {
                logger.warning("User not found with id: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(StandardResponse.error("User not found", "User with id " + id + " does not exist"));
            }
        } catch (Exception e) {
            logger.severe("Failed to delete user with id " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to delete user", e.getMessage()));
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StandardResponse<UserDTO>> getUserByEmail(@PathVariable String email) {
        logger.info("Received request to get user by email: " + email);
        try {
            UserDTO user = userService.getUserByEmail(email);
            logger.info("Successfully retrieved user with email: " + email);
            return ResponseEntity.ok(StandardResponse.success("User retrieved successfully", user));
        } catch (RuntimeException e) {
            logger.warning("User not found with email " + email + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("User not found", e.getMessage()));
        } catch (Exception e) {
            logger.severe("Failed to retrieve user with email " + email + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve user", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<UserDTO>> getUserById(@PathVariable UUID id) {
        logger.info("Received request to get user by id: " + id);
        try {
            UserDTO user = userService.getUserById(id);
            logger.info("Successfully retrieved user with id: " + id);
            return ResponseEntity.ok(StandardResponse.success("User retrieved successfully", user));
        } catch (RuntimeException e) {
            logger.warning("User not found with id " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("User not found", e.getMessage()));
        } catch (Exception e) {
            logger.severe("Failed to retrieve user with id " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve user", e.getMessage()));
        }
    }
}
