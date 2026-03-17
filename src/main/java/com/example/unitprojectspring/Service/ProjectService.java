package com.example.unitprojectspring.Service;
import com.example.unitprojectspring.DTO.ProjectDTO;
import com.example.unitprojectspring.DTO.SectionDTO;
import com.example.unitprojectspring.Entities.Project;
import com.example.unitprojectspring.Entities.Section;
import com.example.unitprojectspring.Entities.User;
import com.example.unitprojectspring.Repositories.ProjectRepository;
import com.example.unitprojectspring.Repositories.SectionRepository;
import com.example.unitprojectspring.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final SectionRepository sectionRepository;

    private final UserRepository userRepository;


    public ProjectService(ProjectRepository projectRepository, SectionRepository sectionRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.sectionRepository = sectionRepository;
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

        return convertProjectToDto(savedProject);
    }

    @Transactional
    public List<ProjectDTO> getProjectByTitle(String title) {
        return projectRepository.findByTitle(title).stream()
                .map(this::convertProjectToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return convertProjectToDto(project);
    }

    public List<ProjectDTO> getAllProjectsWithUserId(Long userId) {
        return projectRepository.findByUserId(userId).stream()
                .map(this::convertProjectToDto)
                .collect(Collectors.toList());
    }

    public ProjectDTO updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setTitle(projectDetails.getTitle());
        project.setDescription(projectDetails.getDescription());
        Project updatedProject = projectRepository.save(project);
        return convertProjectToDto(updatedProject);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }
        projectRepository.deleteById(id);
    }

    @Transactional
    public void createProjectWithSection(Project project, String sectionTitle, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        project.setUser(user);

        Project savedProject = projectRepository.save(project);

        Section initialSection = new Section();
        initialSection.setTitle(sectionTitle);

        initialSection.setProject(savedProject);

        sectionRepository.save(initialSection);
    }

    private ProjectDTO convertProjectToDto(Project projectEntity) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(projectEntity.getId());
        dto.setTitle(projectEntity.getTitle());
        dto.setDescription(projectEntity.getDescription());
        dto.setCompletionPercentage(projectEntity.getCompletionPercentage());

        if (projectEntity.getUser() != null) {
            dto.setUserId(projectEntity.getUser().getId());
        }

        if (projectEntity.getSections() != null) {
            List<SectionDTO> sectionDTOs = projectEntity.getSections().stream()
                    .map(this::convertSectionToDto)
                    .collect(Collectors.toList());
            dto.setSections(sectionDTOs);
        }

        return dto;
    }

    private SectionDTO convertSectionToDto(Section sectionEntity) {
        SectionDTO sectionDto = new SectionDTO();
        sectionDto.setId(sectionEntity.getId());
        sectionDto.setTitle(sectionEntity.getTitle());

        if (sectionEntity.getProject() != null) {
            sectionDto.setProjectId(sectionEntity.getProject().getId());
        }

        if (sectionEntity.getTasks() != null) {
            List<com.example.unitprojectspring.DTO.TaskDTO> taskDTOs = sectionEntity.getTasks().stream()
                    .map(task -> {
                        com.example.unitprojectspring.DTO.TaskDTO t = new com.example.unitprojectspring.DTO.TaskDTO();
                        t.setId(task.getId());
                        t.setTitle(task.getTitle());
                        t.setDescription(task.getDescription());
                        t.setCompleted(task.isCompleted());
                        return t;
                    }).collect(Collectors.toList());
            sectionDto.setTasks(taskDTOs);
        }

        return sectionDto;
    }
}
