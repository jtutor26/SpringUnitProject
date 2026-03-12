package com.example.unitprojectspring.Service;
import com.example.unitprojectspring.DTO.SectionDTO;
import com.example.unitprojectspring.Entities.Project;
import com.example.unitprojectspring.Entities.Section;
import com.example.unitprojectspring.Repositories.ProjectRepository;
import com.example.unitprojectspring.Repositories.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    private final ProjectRepository projectRepository;


    public SectionService(SectionRepository sectionRepository, ProjectRepository projectRepository) {
        this.sectionRepository = sectionRepository;
        this.projectRepository = projectRepository;
    }

    public SectionDTO createSection(Section section, Long project_id) {

        if (project_id == null) {
            throw new IllegalArgumentException("A Section must belong to a Project.");
        }

        Project project = projectRepository.findById(project_id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        section.setProject(project);

        Section savedSection = sectionRepository.save(section);

        return convertToDto(savedSection);
    }

    public SectionDTO getSectionById(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));
        return convertToDto(section);
    }

    public List<SectionDTO> getAllSectionsByProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return project.getSections().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SectionDTO updateSection(Long id, Section sectionDetails) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));
        section.setTitle(sectionDetails.getTitle());
        Section updatedSection = sectionRepository.save(section);
        return convertToDto(updatedSection);
    }

    public void deleteSection(Long id) {
        if (!sectionRepository.existsById(id)) {
            throw new RuntimeException("Section not found");
        }
        sectionRepository.deleteById(id);
    }

    private SectionDTO convertToDto(Section sectionEntity) {
        SectionDTO dto = new SectionDTO();
        dto.setId(sectionEntity.getId());
        dto.setTitle(sectionEntity.getTitle());
        dto.setCompletionPercentage(sectionEntity.getCompletionPercentage());

        if (sectionEntity.getProject() != null) {
            dto.setProjectId(sectionEntity.getProject().getId());
        }
        return dto;
    }
}
