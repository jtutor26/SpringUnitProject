package com.example.unitprojectspring.Service;
import com.example.unitprojectspring.DTO.TaskDTO;
import com.example.unitprojectspring.Entities.Section;
import com.example.unitprojectspring.Entities.Task;
import com.example.unitprojectspring.Repositories.SectionRepository;
import com.example.unitprojectspring.Repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final SectionRepository sectionRepository;

    public TaskService(TaskRepository taskRepository, SectionRepository sectionRepository) {
        this.taskRepository = taskRepository;
        this.sectionRepository = sectionRepository;
    }

    public void createTask(Task task, Long section_id) {

        if (section_id == null) {
            throw new IllegalArgumentException("A Task must belong to a Section.");
        }

        Section section = sectionRepository.findById(section_id)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        task.setSection(section);

        Task savedTask = taskRepository.save(task);

        convertTaskToDto(savedTask);
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return convertTaskToDto(task);
    }

    public List<TaskDTO> getAllTasksBySection(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));
        return section.getTasks().stream()
                .map(this::convertTaskToDto)
                .collect(Collectors.toList());
    }

    public TaskDTO updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());
        Task updatedTask = taskRepository.save(task);
        return convertTaskToDto(updatedTask);
    }

    public TaskDTO toggleTaskCompletion(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setCompleted(!task.isCompleted());
        Task updatedTask = taskRepository.save(task);
        return convertTaskToDto(updatedTask);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    private TaskDTO convertTaskToDto(Task taskEntity) {
        TaskDTO dto = new TaskDTO();
        dto.setId(taskEntity.getId());
        dto.setTitle(taskEntity.getTitle());
        dto.setDescription(taskEntity.getDescription());
        dto.setCompleted(taskEntity.isCompleted());

        if (taskEntity.getSection() != null) {
            dto.setSectionId(taskEntity.getSection().getId());
        }
        return dto;
    }
}
