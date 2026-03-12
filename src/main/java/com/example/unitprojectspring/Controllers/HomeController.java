package com.example.unitprojectspring.Controllers;

import com.example.unitprojectspring.Entities.Project;
import org.springframework.ui.Model;
import com.example.unitprojectspring.DTO.ProjectDTO;
import com.example.unitprojectspring.Entities.User;
import com.example.unitprojectspring.Repositories.UserRepository;
import com.example.unitprojectspring.Service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/dashboard")
public class HomeController {
    private final UserRepository userRepository;
    private final ProjectService projectService;

    public HomeController(UserRepository userRepository, ProjectService projectService) {
        this.userRepository = userRepository;
        this.projectService = projectService;
    }

    @GetMapping("/all")
    public String getallprojects(Model model, Principal principal) {
        String username = principal.getName();

        User currentuser = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ProjectDTO> projects = projectService.getAllProjectsWithUserId(currentuser.getId());

        model.addAttribute("projects", projects);

        return "redirect:/api/dashboard/all";

    }

    @PostMapping("/{project_id}/delete")
    public String deleteproject(@PathVariable("project_id") Long project_id, Principal principal){
        String username = principal.getName();
        User currentuser = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProjectDTO projecttodelete = projectService.getProjectById(project_id);

        if (!projecttodelete.getUserId().equals(currentuser.getId())) {
            return "redirect:/api/dashboard/all?error=unauthorized";
        }
        projectService.deleteProject(project_id);

        return "redirect:/api/dashboard/all";
    }

    @PostMapping("/add")
    public String addproject(@ModelAttribute Project newproject, Principal principal){
        String username = principal.getName();
        User currentuser = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        projectService.createProject(newproject, currentuser.getId());

        return "redirect:/api/dashboard";
    }


}
