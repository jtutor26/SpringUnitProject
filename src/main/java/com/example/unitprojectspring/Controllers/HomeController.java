package com.example.unitprojectspring.Controllers;
import com.example.unitprojectspring.Entities.Project;
import com.example.unitprojectspring.Service.UserService;
import org.springframework.ui.Model;
import com.example.unitprojectspring.DTO.ProjectDTO;
import com.example.unitprojectspring.Entities.User;
import com.example.unitprojectspring.Service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/api/dashboard")
public class HomeController {

    private final UserService userService;
    private final ProjectService projectService;

    public HomeController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping("")
    public String getAllProjects(Model model, Principal principal) {
        User currentUser = userService.getUserFromPrincipal(principal.getName());
        List<ProjectDTO> projects = projectService.getAllProjectsWithUserId(currentUser.getId());
        model.addAttribute("projects", projects);
        return "dashboard";
    }

    @GetMapping("/search")
    public String searchProject(@RequestParam("title") String title, Model model, Principal principal) {

        String lowerCaseSearchTitle = title.toLowerCase();

        List<ProjectDTO> searchedProjects = projectService.getProjectByTitle(lowerCaseSearchTitle);

        model.addAttribute("projects", Objects.requireNonNullElseGet(searchedProjects, List::of));

        model.addAttribute("searchTitle", title);

        return "dashboard";
    }

    @GetMapping("/project/{id}")
    public String viewProjectDetails(@PathVariable Long id, Model model) {

        ProjectDTO project = projectService.getProjectById(id);

        model.addAttribute("project", project);

        return "project-details"; // Loads our new HTML file
    }

    @PostMapping("/{project_id}/delete")
    public String deleteProject(@PathVariable Long project_id, Principal principal){
        User currentUser = userService.getUserFromPrincipal(principal.getName());
        ProjectDTO projectToDelete = projectService.getProjectById(project_id);

        if (!projectToDelete.getUserId().equals(currentUser.getId())) {
            return "redirect:/api/dashboard?error=unauthorized";
        }

        projectService.deleteProject(project_id);

        return "redirect:/api/dashboard";
    }

    @PostMapping("/add")
    public String addProject(
            @ModelAttribute Project newproject,
            @RequestParam("sectionTitle") String sectionTitle,
            Principal principal) {

        if (newproject.getTitle() != null) {
            newproject.setTitle(newproject.getTitle().toLowerCase());
        }

        User currentUser = userService.getUserFromPrincipal(principal.getName());

        projectService.createProjectWithSection(newproject, sectionTitle, currentUser.getId());

        return "redirect:/api/dashboard";
    }
}