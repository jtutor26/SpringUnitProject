package com.example.unitprojectspring.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private int completionPercentage; // Calculated overall progress
    private List<SectionDTO> sections = new ArrayList<>();

    public ProjectDTO() {}

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; } // <-- 2. Add getter
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(int completionPercentage) { this.completionPercentage = completionPercentage; }

    public List<SectionDTO> getSections() { return sections; }
    public void setSections(List<SectionDTO> sections) { this.sections = sections; }
}
