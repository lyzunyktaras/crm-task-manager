package com.sample.crm.api.service;

import com.sample.crm.api.dto.TaskStatisticsDTO;
import com.sample.crm.model.TaskStatus;
import com.sample.crm.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TaskStatisticsService {

    private final TaskRepository taskRepository;

    public TaskStatisticsService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskStatisticsDTO getTaskStatistics() {
        long totalTasks = taskRepository.count();
        long openTasks = taskRepository.countByStatus(TaskStatus.TODO);
        long inProgressTasks = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long completedTasks = taskRepository.countByStatus(TaskStatus.DONE);
        long overdueTasks = taskRepository.findAll().stream()
                .filter(task -> task.getDueDate().isBefore(LocalDate.now()) && task.getStatus() != TaskStatus.DONE)
                .count();

        return new TaskStatisticsDTO(totalTasks, openTasks, inProgressTasks, completedTasks, overdueTasks);
    }
}

