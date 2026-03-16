package com.example.unitprojectspring.Repositories;
import com.example.unitprojectspring.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Custom queries can be defined here if needed later

    List<Project> findByUserId(Long userId);
}
