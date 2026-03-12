package com.example.springunitproject.Service;
import com.example.unitprojectspring.DTO.SectionDTO;
import com.example.unitprojectspring.Entities.Project;
import com.example.unitprojectspring.Entities.Section;
import com.example.unitprojectspring.Repositories.ProjectRepository;
import com.example.unitprojectspring.Repositories.SectionRepository;
import com.example.unitprojectspring.Service.SectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private SectionService sectionService;

    private Project testProject;
    private Section testSection;

    @BeforeEach
    void setUp() {
        // Setup a valid Project
        testProject = new Project();
        testProject.setId(10L);
        testProject.setTitle("Mastering Go and C++");
        testProject.setSections(new ArrayList<>());

        // Setup a valid Section
        testSection = new Section();
        testSection.setId(50L);
        testSection.setTitle("Backend Architecture");
        testSection.setProject(testProject);
    }

    // --- NEW CREATE SECTION TESTS ---

    @Test
    void createSection_Success_SavesAndLinksToProject() {
        // Arrange
        when(projectRepository.findById(10L)).thenReturn(Optional.of(testProject));
        // Tell the mock to just return the section we hand to it when saving
        when(sectionRepository.save(any(Section.class))).thenAnswer(i -> i.getArgument(0));

        Section newSectionInput = new Section();
        newSectionInput.setTitle("New Section");

        // Act
        SectionDTO result = sectionService.createSection(newSectionInput, 10L);

        // Assert
        assertNotNull(result);
        assertEquals("New Section", result.getTitle());
        assertEquals(10L, result.getProjectId(), "The DTO must contain the mapped Project ID");

        // Verify the database interactions happened exactly once in the correct order
        verify(projectRepository, times(1)).findById(10L);
        verify(sectionRepository, times(1)).save(newSectionInput);
    }

    @Test
    void createSection_ThrowsException_WhenProjectIdIsNull() {
        // Arrange
        Section newSectionInput = new Section();
        newSectionInput.setTitle("No Project Section");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sectionService.createSection(newSectionInput, null);
        });

        assertEquals("A Section must belong to a Project.", exception.getMessage());

        // CRITICAL: Prove the repositories were never touched
        verify(projectRepository, never()).findById(anyLong());
        verify(sectionRepository, never()).save(any(Section.class));
    }

    @Test
    void createSection_ThrowsException_WhenProjectNotFound() {
        // Arrange
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        Section newSectionInput = new Section();
        newSectionInput.setTitle("Bad Project Section");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sectionService.createSection(newSectionInput, 999L);
        });

        assertEquals("Project not found", exception.getMessage());

        // CRITICAL: Prove that because the project wasn't found, it stopped before saving
        verify(sectionRepository, never()).save(any(Section.class));
    }


    // --- GET ALL SECTIONS BY PROJECT TESTS ---

    @Test
    void getAllSectionsByProject_Success_ReturnsMappedList() {
        testProject.getSections().add(testSection);
        when(projectRepository.findById(10L)).thenReturn(Optional.of(testProject));

        List<SectionDTO> results = sectionService.getAllSectionsByProject(10L);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(10L, results.get(0).getProjectId());
    }

    @Test
    void getAllSectionsByProject_ProjectNotFound_ThrowsException() {
        when(projectRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> sectionService.getAllSectionsByProject(999L));
    }

    // --- UPDATE SECTION TESTS ---

    @Test
    void updateSection_Success() {
        when(sectionRepository.findById(50L)).thenReturn(Optional.of(testSection));
        when(sectionRepository.save(any(Section.class))).thenAnswer(i -> i.getArgument(0));

        Section updateDetails = new Section();
        updateDetails.setTitle("Updated Architecture");

        SectionDTO result = sectionService.updateSection(50L, updateDetails);

        assertEquals("Updated Architecture", result.getTitle());
        verify(sectionRepository, times(1)).save(any(Section.class));
    }

    // --- DELETE SECTION TESTS ---

    @Test
    void deleteSection_Success() {
        when(sectionRepository.existsById(50L)).thenReturn(true);
        sectionService.deleteSection(50L);
        verify(sectionRepository, times(1)).deleteById(50L);
    }
}