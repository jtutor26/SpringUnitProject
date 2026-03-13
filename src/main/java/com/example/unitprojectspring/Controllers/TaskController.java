package com.example.unitprojectspring.Controllers;

import com.example.unitprojectspring.DTO.TaskDTO;
import com.example.unitprojectspring.Service.TaskService;
import com.example.unitprojectspring.Entities.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Create Task
    @PostMapping("section/{sectionId}")
    public TaskDTO createTask(@RequestBody Task task, @PathVariable Long sectionId) {
        return taskService.createTask(task, sectionId);
    }

    // Get Task By Id
    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    // Get All Tasks By Section
    @GetMapping("/{section}")
    public List<TaskDTO> getAllTasksBySection(@PathVariable Long section) {
        return taskService.getAllTasksBySection(section);
    }

    // Update Tasks
    @PutMapping("/{id}/update")
    public TaskDTO updateTask(@PathVariable Long id,
                              @RequestBody TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCompleted(taskDTO.isCompleted());
        return taskService.updateTask(id, task);
    }

    // Toggle Task Completion
    @PatchMapping("/{id}/toggle")
    public TaskDTO toggleTaskCompletion(@PathVariable Long id) {
        return taskService.toggleTaskCompletion(id);
    }


    // Delete Task
    @DeleteMapping("/{id}/delete")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
