package com.example.unitprojectspring.Service;
import com.example.unitprojectspring.DTO.ProjectDTO;
import com.example.unitprojectspring.Entities.Project;
import com.example.unitprojectspring.Entities.User;
import com.example.unitprojectspring.Repositories.ProjectRepository;
import com.example.unitprojectspring.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;


    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectDTO createProject(Project project, Long user_id) {

        if (user_id == null) {
            throw new IllegalArgumentException("A Project must belong to a User.");
        }

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        project.setUser(user);

        Project savedProject = projectRepository.save(project);

        return convertToDto(savedProject);
    }

    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return convertToDto(project);
    }

    public List<ProjectDTO> getAllProjectsWithUserId(Long userId) {
        return projectRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProjectDTO updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setTitle(projectDetails.getTitle());
        project.setDescription(projectDetails.getDescription());
        Project updatedProject = projectRepository.save(project);
        return convertToDto(updatedProject);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }
        projectRepository.deleteById(id);
    }

    private ProjectDTO convertToDto(Project projectEntity) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(projectEntity.getId());
        dto.setTitle(projectEntity.getTitle());
        dto.setDescription(projectEntity.getDescription());
        dto.setCompletionPercentage(projectEntity.getCompletionPercentage());

        if (projectEntity.getUser() != null) {
            dto.setUserId(projectEntity.getUser().getId());
        }
        return dto;
    }
}
