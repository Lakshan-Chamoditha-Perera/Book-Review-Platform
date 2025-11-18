package com.bookreviewplatform.userservice.service.custom;

import com.bookreviewplatform.userservice.dto.UserDTO;
import com.bookreviewplatform.userservice.dto.UserRequestDTO;
import com.bookreviewplatform.userservice.entity.UserEntity;
import com.bookreviewplatform.userservice.exception.UserNotFoundException;
import com.bookreviewplatform.userservice.payloads.StandardResponse;
import com.bookreviewplatform.userservice.repository.UserRepository;
import com.bookreviewplatform.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserRepository userRepository;

    @Override
    public StandardResponse getAllUsers() {
        try{
            logger.fine("Fetching all users from database");
            List<UserDTO> users = userRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            logger.fine("Found " + users.size() + " users in database");
            return StandardResponse.success(users);
        } catch (RuntimeException e) {
            return StandardResponse.error("Failed to retrieve users", e.getMessage());
        }
    }

    @Override
    public StandardResponse<UserDTO> saveUser(UserRequestDTO userRequestDTO) {
        try {
            logger.fine("Creating new user with username: " + userRequestDTO.getUsername() + 
                        " and email: " + userRequestDTO.getEmail());
            
            UserEntity userEntity = UserEntity.builder()
                    .username(userRequestDTO.getUsername())
                    .password(userRequestDTO.getPassword())
                    .email(userRequestDTO.getEmail())
                    .build();
            
            UserEntity savedUser = userRepository.save(userEntity);
            logger.info("User created successfully with id: " + savedUser.getId());
            return StandardResponse.success("User created successfully", convertToDTO(savedUser));
        } catch (Exception e) {
            logger.severe("Error creating user: " + e.getMessage());
            return StandardResponse.error("Failed to create user", e.getMessage());
        }
    }

    @Override
    public StandardResponse<Boolean> deleteUser(UUID id) {
        try {
            logger.fine("Checking if user exists with id: " + id);
            if (!userRepository.existsById(id)) {
                logger.warning("User not found with id: " + id);
                return StandardResponse.error("User not found", "User with id " + id + " does not exist");
            }
            logger.fine("Deleting user with id: " + id);
            userRepository.deleteById(id);
            logger.info("User deleted successfully with id: " + id);
            return StandardResponse.success("User deleted successfully", true);
        } catch (Exception e) {
            logger.severe("Error deleting user: " + e.getMessage());
            return StandardResponse.error("Failed to delete user", e.getMessage());
        }
    }

    @Override
    public StandardResponse<UserDTO> getUserByEmail(String email) {
        try {
            logger.fine("Searching for user with email: " + email);
            UserEntity userEntity = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.severe("User not found with email: " + email);
                        return new UserNotFoundException("User not found with email: " + email);
                    });
            logger.fine("User found with email: " + email);
            return StandardResponse.success("User retrieved successfully", convertToDTO(userEntity));
        } catch (UserNotFoundException e) {
            logger.warning("User not found with email: " + email);
            return StandardResponse.error("User not found", e.getMessage());
        } catch (Exception e) {
            logger.severe("Error retrieving user: " + e.getMessage());
            return StandardResponse.error("Failed to retrieve user", e.getMessage());
        }
    }

    @Override
    public StandardResponse getUserById(UUID id) {
        try {
            logger.fine("Searching for user with id: " + id);
            UserEntity userEntity = userRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.severe("User not found with id: " + id);
                        return new UserNotFoundException("User not found with id: " + id);
                    });
            logger.fine("User found with id: " + id);
            return StandardResponse.success("User retrieved successfully", convertToDTO(userEntity));
        } catch (UserNotFoundException e) {
            logger.warning("User not found with id: " + id);
            return StandardResponse.error("User not found", e.getMessage());
        } catch (Exception e) {
            logger.severe("Error retrieving user: " + e.getMessage());
            return StandardResponse.error("Failed to retrieve user", e.getMessage());
        }
    }

    private UserDTO convertToDTO(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
    }
}
