
package com.example.unitprojectspring.Service;
import com.example.unitprojectspring.DTO.UserDTO;
import com.example.unitprojectspring.DTO.UserRegistrationDTO;
import com.example.unitprojectspring.Entities.User;
import com.example.unitprojectspring.Repositories.UserRepository;
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

        // 2. Instantiate a new User entity
        User newUser = new User();

        // 3. Map the clean data from the DTO to the Entity
        newUser.setUsername(registrationDTO.getUsername());
        newUser.setEmail(registrationDTO.getEmail());

        // 4. THE CRITICAL STEP: Hash the password before setting it
        String hashedPassword = passwordEncoder.encode(registrationDTO.getPassword());
        newUser.setPassword(hashedPassword);

        // 5. Save the user to the database
        userRepository.save(newUser);

        return convertToDto(newUser);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserRegistrationDTO registrationDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }


    public UserDTO convertToDto(User userEntity) {
        UserDTO dto = new UserDTO();
        dto.setUsername(userEntity.getUsername());
        dto.setEmail(userEntity.getEmail());
        dto.setCreatedAt(userEntity.getCreatedAt());
        return dto;
    }

    public UserRegistrationDTO convertToRegistrationDto(User userEntity) {
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setUsername(userEntity.getUsername());
        dto.setEmail(userEntity.getEmail());
        dto.setPassword(userEntity.getPassword());
        return dto;
    }
}
