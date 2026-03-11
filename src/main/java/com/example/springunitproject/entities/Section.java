package com.example.springunitproject.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public Section() {}

    public Section(String title) {
        this.title = title;
    }

    @Transient
    public int getCompletionPercentage() {
        if (tasks == null || tasks.isEmpty()) {
            return 0;
        }
        long completedTasks = tasks.stream().filter(Task::isCompleted).count();
        return (int) Math.round((completedTasks * 100.0) / tasks.size());
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setSection(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setSection(null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
