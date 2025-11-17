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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO saveUser(UserRequestDTO userRequestDTO) {
        UserEntity userEntity = UserEntity.builder()
                .username(userRequestDTO.getUsername())
                .password(userRequestDTO.getPassword())
                .email(userRequestDTO.getEmail())
                .build();
        
        UserEntity savedUser = userRepository.save(userEntity);
        return convertToDTO(savedUser);
    }

    @Override
    public Boolean deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
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
