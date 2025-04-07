package com.sample.crm.controller;

import com.sample.crm.dto.TaskDTO;
import com.sample.crm.model.TaskStatus;
import com.sample.crm.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void shouldReturnAllTasks() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setDescription("Test Task");
        List<TaskDTO> taskDTOList = List.of(taskDTO);

        when(taskService.findAll()).thenReturn(taskDTOList);

        ResponseEntity<List<TaskDTO>> response = taskController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(taskDTOList);
        verify(taskService, times(1)).findAll();
    }

    @Test
    void shouldReturnTasksByClient() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setDescription("Test Task");
        List<TaskDTO> taskDTOList = List.of(taskDTO);

        when(taskService.findByClient(1L)).thenReturn(taskDTOList);

        ResponseEntity<List<TaskDTO>> response = taskController.findByClient(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(taskDTOList);
        verify(taskService, times(1)).findByClient(1L);
    }

    @Test
    void shouldCreateTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescription("New Task");

        TaskDTO savedTaskDTO = new TaskDTO();
        savedTaskDTO.setId(1L);
        savedTaskDTO.setDescription("New Task");

        when(taskService.create(taskDTO)).thenReturn(savedTaskDTO);

        ResponseEntity<TaskDTO> response = taskController.create(taskDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(savedTaskDTO);
        verify(taskService, times(1)).create(taskDTO);
    }

    @Test
    void shouldUpdateTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setDescription("Updated Task");

        when(taskService.update(1L, taskDTO)).thenReturn(taskDTO);

        ResponseEntity<TaskDTO> response = taskController.update(1L, taskDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(taskDTO);
        verify(taskService, times(1)).update(1L, taskDTO);
    }

    @Test
    void shouldDeleteTask() {
        doNothing().when(taskService).delete(1L);

        ResponseEntity<Void> response = taskController.delete(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(taskService, times(1)).delete(1L);
    }

    @Test
    void shouldUpdateTaskStatus() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setDescription("Updated Task Status");

        when(taskService.updateStatus(1L, TaskStatus.IN_PROGRESS)).thenReturn(taskDTO);

        ResponseEntity<TaskDTO> response = taskController.updateStatus(1L, TaskStatus.IN_PROGRESS);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(taskDTO);
        verify(taskService, times(1)).updateStatus(1L, TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldAssignContactToTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setDescription("Task with assigned contact");

        when(taskService.assignContact(1L, 2L)).thenReturn(taskDTO);

        ResponseEntity<TaskDTO> response = taskController.assignContact(1L, 2L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(taskDTO);
        verify(taskService, times(1)).assignContact(1L, 2L);
    }
}
