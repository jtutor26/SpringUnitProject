package com.example.unitprojectspring.Controllers;

import com.example.unitprojectspring.DTO.UserDTO;
import com.example.unitprojectspring.DTO.UserRegistrationDTO;
import com.example.unitprojectspring.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Return all users
    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get users by their id
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Update the users information
    @PutMapping("/{id}/update")
    public UserDTO updateUser(@PathVariable Long id,
                              @RequestBody UserRegistrationDTO registrationDTO) {
        return userService.updateUser(id, registrationDTO);
    }

    // Delete the user from the system
    @DeleteMapping("/{id}/delete")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
