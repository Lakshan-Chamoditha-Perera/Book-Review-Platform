package com.bookreviewplatform.userservice.dto;

import com.bookreviewplatform.userservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private Role role; // ADMIN or USER
}