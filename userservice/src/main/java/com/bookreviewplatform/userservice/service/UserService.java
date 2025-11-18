package com.bookreviewplatform.userservice.service;

import com.bookreviewplatform.userservice.dto.UserDTO;
import com.bookreviewplatform.userservice.dto.UserRequestDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO saveUser(UserRequestDTO userRequestDTO);

    Boolean deleteUser(UUID id);

    UserDTO getUserByEmail(String email);

    UserDTO getUserById(UUID id);
}
