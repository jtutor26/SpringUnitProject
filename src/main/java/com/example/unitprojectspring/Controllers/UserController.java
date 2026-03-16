package com.example.unitprojectspring.Controllers;

import com.example.unitprojectspring.DTO.UserRegistrationDTO;
import com.example.unitprojectspring.Entities.User;
import com.example.unitprojectspring.Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        User currentUser = userService.getUserFromPrincipal(principal.getName());

        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setUsername(currentUser.getUsername());
        dto.setEmail(currentUser.getEmail());

        model.addAttribute("userDto", dto);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("userDto") UserRegistrationDTO userDto, Principal principal) {
        User currentUser = userService.getUserFromPrincipal(principal.getName());
        userService.updateUser(currentUser.getId(), userDto);

        return "redirect:/users/profile?success";
    }

    @PostMapping("/profile/delete")
    public String deleteProfile(Principal principal, HttpServletRequest request) throws ServletException {
        User currentUser = userService.getUserFromPrincipal(principal.getName());

        userService.deleteUser(currentUser.getId());

        request.logout();

        return "redirect:/register?deleted";
    }
}