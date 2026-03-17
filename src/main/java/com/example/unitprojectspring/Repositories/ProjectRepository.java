package com.example.unitprojectspring.Repositories;
import com.example.unitprojectspring.Entities.Project;
import com.example.unitprojectspring.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Custom queries can be defined here if needed later

    List<Project> findByUserId(Long userId);

    Optional<Project> findByTitle(String title);
}
