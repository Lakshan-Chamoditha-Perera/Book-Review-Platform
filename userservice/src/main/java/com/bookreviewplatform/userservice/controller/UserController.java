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

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<StandardResponse<List<UserDTO>>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(StandardResponse.success("Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve users", e.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<StandardResponse<UserDTO>> saveUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            UserDTO savedUser = userService.saveUser(userRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(StandardResponse.success("User created successfully", savedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.error("Failed to create user", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<Boolean>> deleteUser(@PathVariable UUID id) {
        try {
            Boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return ResponseEntity.ok(StandardResponse.success("User deleted successfully", true));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(StandardResponse.error("User not found", "User with id " + id + " does not exist"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to delete user", e.getMessage()));
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StandardResponse<UserDTO>> getUserByEmail(@PathVariable String email) {
        try {
            UserDTO user = userService.getUserByEmail(email);
            return ResponseEntity.ok(StandardResponse.success("User retrieved successfully", user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("User not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to retrieve user", e.getMessage()));
        }
    }
}
