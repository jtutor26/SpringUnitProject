package com.example.unitprojectspring.Entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Assuming you have a User entity created for authentication
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // A Project has many Sections.
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Project() {}

    public Project(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // --- Dynamic Overall Completion Calculation ---
    @Transient
    public int getCompletionPercentage() {
        if (sections == null || sections.isEmpty()) {
            return 0;
        }

        long totalTasks = 0;
        long completedTasks = 0;

        for (Section section : sections) {
            List<Task> sectionTasks = section.getTasks();
            if (sectionTasks != null) {
                totalTasks += sectionTasks.size();
                completedTasks += sectionTasks.stream().filter(Task::isCompleted).count();
            }
        }

        if (totalTasks == 0) {
            return 0;
        }

        return (int) Math.round((completedTasks * 100.0) / totalTasks);
    }

    // Helper method to keep relationships in sync
    public void addSection(Section section) {
        sections.add(section);
        section.setProject(this);
    }

    public void removeSection(Section section) {
        sections.remove(section);
        section.setProject(null);
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Section> getSections() { return sections; }
    public void setSections(List<Section> sections) { this.sections = sections; }
}
