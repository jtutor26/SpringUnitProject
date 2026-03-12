package com.example.springunitproject.Service;
import com.example.unitprojectspring.DTO.ProjectDTO;
import com.example.unitprojectspring.Entities.Project;
import com.example.unitprojectspring.Entities.User;
import com.example.unitprojectspring.Repositories.ProjectRepository;
import com.example.unitprojectspring.Repositories.UserRepository;
import com.example.unitprojectspring.Service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project testProject;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Setup dummy user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        // Setup dummy project
        testProject = new Project();
        testProject.setId(100L);
        testProject.setTitle("New App Build");
        testProject.setDescription("Building the heartbeat monitor");
    }

    // --- CREATE PROJECT TESTS ---

    @Test
    void createProject_WithUserId_SuccessfullySavesAndLinksToUser() {
        // Arrange
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // We don't necessarily need to mock userRepository.save() returning anything
        // since the service doesn't use the return value of that specific save.

        // Act
        ProjectDTO result = projectService.createProject(testProject, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("New App Build", result.getTitle());

        // Verify both repositories were interacted with correctly
        verify(projectRepository, times(1)).save(testProject);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void createProject_WithoutUserId_SavesProjectOnly() {
        // Arrange
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        // Act
        ProjectDTO result = projectService.createProject(testProject, null);

        // Assert
        assertNotNull(result);
        verify(projectRepository, times(1)).save(testProject);

        // Verify the user repository is NEVER touched if user_id is null
        verify(userRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    // --- GET PROJECT TESTS ---

    @Test
    void getProjectById_Success() {
        // Arrange
        when(projectRepository.findById(100L)).thenReturn(Optional.of(testProject));

        // Act
        ProjectDTO result = projectService.getProjectById(100L);

        // Assert
        assertNotNull(result);
        assertEquals("New App Build", result.getTitle());
    }

    @Test
    void getProjectById_ThrowsException_WhenNotFound() {
        // Arrange
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.getProjectById(999L);
        });
        assertEquals("Project not found", exception.getMessage());
    }

    // --- UPDATE PROJECT TESTS ---

    @Test
    void updateProject_Success() {
        // Arrange
        when(projectRepository.findById(100L)).thenReturn(Optional.of(testProject));

        Project updatedDetails = new Project();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setDescription("Updated Description");

        // Tell mock to just return the object it was handed during the save
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ProjectDTO result = projectService.updateProject(100L, updatedDetails);

        // Assert
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void updateProject_ThrowsException_WhenNotFound() {
        // Arrange
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());
        Project dummyDetails = new Project();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> projectService.updateProject(999L, dummyDetails));
        verify(projectRepository, never()).save(any(Project.class));
    }

    // --- DELETE PROJECT TESTS ---

    @Test
    void deleteProject_Success() {
        // Arrange
        when(projectRepository.existsById(100L)).thenReturn(true);

        // Act
        projectService.deleteProject(100L);

        // Assert
        verify(projectRepository, times(1)).deleteById(100L);
    }

    @Test
    void deleteProject_ThrowsException_WhenNotFound() {
        // Arrange
        when(projectRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> projectService.deleteProject(999L));
        verify(projectRepository, never()).deleteById(anyLong());
    }
}
