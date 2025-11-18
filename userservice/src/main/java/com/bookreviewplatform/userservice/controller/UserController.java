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

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<StandardResponse> getAllUsers() {
        logger.info("Received request to get all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping()
    public ResponseEntity<StandardResponse> saveUser(@RequestBody UserRequestDTO userRequestDTO) {
        logger.info("Received request to create user with email: " + userRequestDTO.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(userRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteUser(@PathVariable UUID id) {
        logger.info("Received request to delete user with id: " + id);
        return ResponseEntity.ok(userService.deleteUser(id));

    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StandardResponse> getUserByEmail(@PathVariable String email) {
        logger.info("Received request to get user by email: " + email);
        return ResponseEntity.ok(userService.getUserByEmail(email));

    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getUserById(@PathVariable UUID id) {
        logger.info("Received request to get user by id: " + id);
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
