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
public class AuthResponse {
    private String token;
    private String username;
    private Role role;
    private String message;
}
