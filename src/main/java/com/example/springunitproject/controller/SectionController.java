package com.example.springunitproject.controller;

import com.example.springunitproject.dto.SectionDTO;
import com.example.springunitproject.entities.Section;
import com.example.springunitproject.service.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<SectionDTO> createSection(@RequestBody Section section, @RequestParam Long projectId) {
        return ResponseEntity.ok(sectionService.createSection(section, projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectionDTO> getSectionById(@PathVariable Long id) {
        return ResponseEntity.ok(sectionService.getSectionById(id));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<SectionDTO>> getAllSectionsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(sectionService.getAllSectionsByProject(projectId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectionDTO> updateSection(@PathVariable Long id, @RequestBody Section sectionDetails) {
        return ResponseEntity.ok(sectionService.updateSection(id, sectionDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}
