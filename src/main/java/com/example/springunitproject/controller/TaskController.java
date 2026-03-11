package com.example.springunitproject.controller;

import com.example.springunitproject.dto.TaskDTO;
import com.example.springunitproject.entities.Task;
import com.example.springunitproject.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody Task task, @RequestParam Long sectionId) {
        return ResponseEntity.ok(taskService.createTask(task, sectionId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<TaskDTO>> getAllTasksBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(taskService.getAllTasksBySection(sectionId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDetails));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TaskDTO> toggleTaskCompletion(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.toggleTaskCompletion(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
