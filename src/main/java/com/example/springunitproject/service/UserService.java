package com.example.springunitproject.service;

import com.example.springunitproject.dto.UserDTO;
import com.example.springunitproject.dto.UserRegistrationDTO;
import com.example.springunitproject.entities.User;
import com.example.springunitproject.repositories.UserRepository;
import com.example.springunitproject.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User newUser = new User();
        newUser.setUsername(registrationDTO.getUsername());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        userRepository.save(newUser);
        return convertToDto(newUser);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDto(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserRegistrationDTO registrationDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        if (registrationDTO.getPassword() != null && !registrationDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        }

        userRepository.save(user);
        return convertToDto(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserDTO convertToDto(User userEntity) {
        UserDTO dto = new UserDTO();
        dto.setId(userEntity.getId());
        dto.setUsername(userEntity.getUsername());
        dto.setEmail(userEntity.getEmail());
        dto.setCreatedAt(userEntity.getCreatedAt());
        return dto;
    }
}
