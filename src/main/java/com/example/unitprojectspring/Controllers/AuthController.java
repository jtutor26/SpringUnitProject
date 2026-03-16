package com.example.unitprojectspring.Controllers;

import com.example.unitprojectspring.Service.UserService;
import org.springframework.ui.Model;
import com.example.unitprojectspring.DTO.UserRegistrationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDTO registrationDTO, Model model) {

        try {
            userService.registerUser(registrationDTO);

            return "redirect:/api/dashboard";

        } catch (RuntimeException e) {

            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

}
