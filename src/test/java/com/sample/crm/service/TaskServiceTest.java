package com.sample.crm.service;

import com.sample.crm.dto.ClientDTO;
import com.sample.crm.dto.ContactDTO;
import com.sample.crm.dto.TaskDTO;
import com.sample.crm.entity.Client;
import com.sample.crm.entity.Contact;
import com.sample.crm.entity.Task;
import com.sample.crm.entity.User;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.mapper.TaskMapper;
import com.sample.crm.model.TaskStatus;
import com.sample.crm.repository.ClientRepository;
import com.sample.crm.repository.ContactRepository;
import com.sample.crm.repository.TaskRepository;
import com.sample.crm.service.notification.UserNotifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserNotifier userNotifier;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskDTO taskDTO;
    private Contact contact;
    private Client client;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("username");

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);

        client = new Client();
        client.setId(1L);
        client.setUsers(List.of(user));

        contact = new Contact();
        contact.setId(1L);
        contact.setClient(client);

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(1L);
        contactDTO.setClient(clientDTO);

        task = new Task();
        task.setId(1L);
        task.setDescription("Test Task");
        task.setStatus(TaskStatus.TODO);
        task.setClient(client);
        task.setContact(contact);

        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setDescription("Test Task");
        taskDTO.setStatus(TaskStatus.TODO);
        taskDTO.setClient(clientDTO);
        taskDTO.setContact(contactDTO);
    }

    @Test
    void shouldFindAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        List<TaskDTO> result = taskService.findAll();

        assertEquals(1, result.size());
        assertEquals(taskDTO.getId(), result.get(0).getId());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void shouldFindTasksByClient() {
        Long clientId = 1L;
        when(taskRepository.findByClientId(clientId)).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        List<TaskDTO> result = taskService.findByClient(clientId);

        assertEquals(1, result.size());
        assertEquals(taskDTO.getId(), result.get(0).getId());
        verify(taskRepository, times(1)).findByClientId(clientId);
    }

    @Test
    void shouldCreateTask() {
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.create(taskDTO);

        assertEquals(taskDTO.getId(), result.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldUpdateTask() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(new Client()));
        when(contactRepository.findById(anyLong())).thenReturn(Optional.of(contact));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.update(task.getId(), taskDTO);

        assertEquals(taskDTO.getId(), result.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundOnUpdate() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> taskService.update(task.getId(), taskDTO)
        );

        assertEquals("NOT_FOUND", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldDeleteTask() {
        taskService.delete(task.getId());
        verify(taskRepository, times(1)).deleteById(task.getId());
    }

    @Test
    void shouldUpdateTaskStatus() {
        task.setStatus(TaskStatus.TODO);
        task.setContact(contact);
        contact.setClient(client);
        client.setUsers(List.of(user));

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenAnswer(invocation -> invocation.getArgument(0));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        taskService.updateStatus(task.getId(), TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus(), "Task status should be updated to IN_PROGRESS");
        verify(taskRepository, times(1)).save(task);
        verify(userNotifier, times(1)).notifyOfTaskStatusChange(eq(user), eq(taskDTO));
    }

}
