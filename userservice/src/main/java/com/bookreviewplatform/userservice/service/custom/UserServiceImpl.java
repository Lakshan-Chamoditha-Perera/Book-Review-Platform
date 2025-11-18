package com.bookreviewplatform.userservice.service.custom;

import com.bookreviewplatform.userservice.dto.UserDTO;
import com.bookreviewplatform.userservice.dto.UserRequestDTO;
import com.bookreviewplatform.userservice.entity.UserEntity;
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
    public List<UserDTO> getAllUsers() {
        logger.fine("Fetching all users from database");
        List<UserDTO> users = userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.fine("Found " + users.size() + " users in database");
        return users;
    }

    @Override
    public UserDTO saveUser(UserRequestDTO userRequestDTO) {
        logger.fine("Creating new user with username: " + userRequestDTO.getUsername() + 
                    " and email: " + userRequestDTO.getEmail());
        
        UserEntity userEntity = UserEntity.builder()
                .username(userRequestDTO.getUsername())
                .password(userRequestDTO.getPassword())
                .email(userRequestDTO.getEmail())
                .build();
        
        UserEntity savedUser = userRepository.save(userEntity);
        logger.info("User created successfully with id: " + savedUser.getId());
        return convertToDTO(savedUser);
    }

    @Override
    public Boolean deleteUser(UUID id) {
        logger.fine("Checking if user exists with id: " + id);
        if (!userRepository.existsById(id)) {
            logger.warning("User not found with id: " + id);
            return false;
        }
        logger.fine("Deleting user with id: " + id);
        userRepository.deleteById(id);
        logger.info("User deleted successfully with id: " + id);
        return true;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        logger.fine("Searching for user with email: " + email);
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.severe("User not found with email: " + email);
                    return new RuntimeException("User not found with email: " + email);
                });
        logger.fine("User found with email: " + email);
        return convertToDTO(userEntity);
    }

    @Override
    public UserDTO getUserById(UUID id) {
        logger.fine("Searching for user with id: " + id);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.severe("User not found with id: " + id);
                    return new RuntimeException("User not found with id: " + id);
                });
        logger.fine("User found with id: " + id);
        return convertToDTO(userEntity);
    }

    private UserDTO convertToDTO(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
    }
}
