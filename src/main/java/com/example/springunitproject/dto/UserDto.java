package com.example.springunitproject.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    private long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;

    private List<ProjectDTO> projects = new ArrayList<>();

    public UserDTO() {}

    public UserDTO(long id, String username, String email, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ProjectDTO> getProjects() { return projects; }
    public void setProjects(List<ProjectDTO> projects) { this.projects = projects; }
}
