package com.example.springunitproject.service;

import com.example.springunitproject.dto.ProjectDTO;
import com.example.springunitproject.entities.Project;
import com.example.springunitproject.entities.User;
import com.example.springunitproject.repositories.ProjectRepository;
import com.example.springunitproject.repositories.UserRepository;
import com.example.springunitproject.exception.ResourceNotFoundException;
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
        Project savedProject = projectRepository.save(project);

        if (user_id != null) {
            java.util.Optional<User> userOpt = userRepository.findById(user_id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                user.addProject(savedProject);

                userRepository.save(user);
            }
        }

        return convertToDto(savedProject);
    }

    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return convertToDto(project);
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProjectDTO updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        project.setTitle(projectDetails.getTitle());
        project.setDescription(projectDetails.getDescription());
        Project updatedProject = projectRepository.save(project);
        return convertToDto(updatedProject);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found");
        }
        projectRepository.deleteById(id);
    }

    private ProjectDTO convertToDto(Project projectEntity) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(projectEntity.getId());
        dto.setTitle(projectEntity.getTitle());
        dto.setDescription(projectEntity.getDescription());
        dto.setCompletionPercentage(projectEntity.getCompletionPercentage());
        return dto;
    }
}
