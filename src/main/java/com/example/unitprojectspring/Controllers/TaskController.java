package com.example.unitprojectspring.Controllers;

import com.example.unitprojectspring.Entities.Task;
import com.example.unitprojectspring.Service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/section/{sectionId}/add")
    public String createTask(@ModelAttribute Task task, @PathVariable Long sectionId, HttpServletRequest request) {
        taskService.createTask(task, sectionId);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/api/dashboard");
    }

    @PostMapping("/{id}/update")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task taskDTO, HttpServletRequest request) {

        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCompleted(taskDTO.isCompleted());

        taskService.updateTask(id, task);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/api/dashboard");
    }

    @PostMapping("/{id}/toggle")
    public String toggleTaskCompletion(@PathVariable Long id, HttpServletRequest request) {

        taskService.toggleTaskCompletion(id);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/api/dashboard");
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, HttpServletRequest request) {

        taskService.deleteTask(id);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/api/dashboard");
    }
}