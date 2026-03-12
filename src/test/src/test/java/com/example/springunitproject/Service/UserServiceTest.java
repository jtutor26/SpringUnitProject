package com.example.springunitproject.Service;
import com.example.unitprojectspring.DTO.UserDTO;
import com.example.unitprojectspring.DTO.UserRegistrationDTO;
import com.example.unitprojectspring.Entities.User;
import com.example.unitprojectspring.Repositories.UserRepository;
import com.example.unitprojectspring.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Tells JUnit to use Mockito to fake the dependencies
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks // Automatically injects the two mocks above into the UserService
    private UserService userService;

    private User testUser;
    private UserRegistrationDTO registrationDTO;

    // This runs before EVERY test to give us fresh dummy data
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashed_password");
        testUser.setCreatedAt(LocalDateTime.now());

        registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUsername("testuser");
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setPassword("raw_password");
    }

    // --- REGISTER USER TESTS ---

    @Test
    void registerUser_Success() {
        // Arrange: Tell the mocks how to behave
        when(userRepository.existsByEmail(registrationDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationDTO.getPassword())).thenReturn("new_hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserDTO result = userService.registerUser(registrationDTO);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class)); // Verifies save() was called exactly once
    }

    @Test
    void registerUser_ThrowsException_WhenEmailExists() {
        // Arrange
        when(userRepository.existsByEmail(registrationDTO.getEmail())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(registrationDTO);
        });
        assertEquals("Email already in use", exception.getMessage());

        // Verify save was NEVER called because the error stopped the process
        verify(userRepository, never()).save(any(User.class));
    }

    // --- GET USER TESTS ---

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserDTO result = userService.getUserById(1L);

        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getUserById_ThrowsException_WhenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(99L));
    }

    @Test
    void getAllUsers_Success() {
        User user2 = new User();
        user2.setUsername("user2");
        when(userRepository.findAll()).thenReturn(List.of(testUser, user2));

        List<UserDTO> results = userService.getAllUsers();

        assertEquals(2, results.size());
        assertEquals("testuser", results.get(0).getUsername());
    }

    // --- UPDATE USER TESTS ---

    @Test
    void updateUser_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("new_raw_password")).thenReturn("new_hashed_password");

        UserRegistrationDTO updateDTO = new UserRegistrationDTO();
        updateDTO.setUsername("updatedUser");
        updateDTO.setEmail("updated@example.com");
        updateDTO.setPassword("new_raw_password");

        // Act
        UserDTO result = userService.updateUser(1L, updateDTO);

        // Assert
        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
        verify(userRepository, times(1)).save(testUser);
    }

    // --- DELETE USER TESTS ---

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_ThrowsException_WhenNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(99L));
        verify(userRepository, never()).deleteById(anyLong());
    }
}
