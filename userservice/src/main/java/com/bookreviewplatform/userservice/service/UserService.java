package com.bookreviewplatform.userservice.service;

import com.bookreviewplatform.userservice.dto.UserRequestDTO;
import com.bookreviewplatform.userservice.payloads.StandardResponse;

import java.util.UUID;

public interface UserService {
    StandardResponse getAllUsers();

    StandardResponse saveUser(UserRequestDTO userRequestDTO);

    StandardResponse deleteUser(UUID id);

    StandardResponse getUserByEmail(String email);

    StandardResponse getUserById(UUID id);
}
