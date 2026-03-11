package com.example.springunitproject.dto;

import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {

    private Long id;
    private String title;
    private String description;
    private int completionPercentage;

    private List<SectionDTO> sections = new ArrayList<>();

    public ProjectDTO() {}

    public ProjectDTO(Long id, String title, String description, int completionPercentage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completionPercentage = completionPercentage;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(int completionPercentage) { this.completionPercentage = completionPercentage; }

    public List<SectionDTO> getSections() { return sections; }
    public void setSections(List<SectionDTO> sections) { this.sections = sections; }
}
