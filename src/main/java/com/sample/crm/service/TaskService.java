package com.sample.crm.service;

import java.time.LocalDateTime;
import java.util.List;

import com.sample.crm.dto.TaskDTO;
import com.sample.crm.entity.Contact;
import com.sample.crm.entity.Task;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.exception.model.ExceptionMessage;
import com.sample.crm.mapper.TaskMapper;
import com.sample.crm.model.TaskStatus;
import com.sample.crm.repository.ClientRepository;
import com.sample.crm.repository.ContactRepository;
import com.sample.crm.repository.TaskRepository;
import com.sample.crm.service.notification.UserNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final ContactRepository contactRepository;
  private final ClientRepository clientRepository;
  private final UserNotifier userNotifier;

  /**
   * Retrieves all tasks, with caching enabled.
   *
   * @return List of all tasks as TaskDTO.
   */
  @Cacheable(value = "tasks")
  @Transactional(readOnly = true)
  public List<TaskDTO> findAll() {
    log.debug("Fetching all tasks from the database");
    return taskRepository.findAll().stream().map(taskMapper::toDto).toList();
  }

  /**
   * Retrieves tasks associated with a specific client, with caching enabled.
   *
   * @param id The client ID.
   * @return List of tasks for the specified client as TaskDTO.
   */
  @Cacheable(value = "tasks", key = "#id")
  @Transactional(readOnly = true)
  public List<TaskDTO> findByClient(Long id) {
    log.debug("Fetching tasks for client with ID: {}", id);
    return taskRepository.findByClientId(id).stream().map(taskMapper::toDto).toList();
  }

  /**
   * Creates a new task and clears the cache.
   *
   * @param taskDTO The task data to create.
   * @return The created task as TaskDTO.
   */
  @Transactional
  @CacheEvict(value = "tasks", allEntries = true)
  public TaskDTO create(TaskDTO taskDTO) {
    log.debug("Creating a new task: {}", taskDTO);
    return taskMapper.toDto(taskRepository.save(taskMapper.toEntity(taskDTO)));
  }

  /**
   * Updates an existing task and clears the cache. Notifies users if the status changes.
   *
   * @param id      The ID of the task to update.
   * @param taskDTO The updated task data.
   * @return The updated task as TaskDTO.
   */
  @Transactional
  @CacheEvict(value = "tasks", allEntries = true)
  public TaskDTO update(Long id, TaskDTO taskDTO) {
    log.debug("Updating task with ID: {}", id);
    Task task = getTask(id);

    if (taskDTO.getStatus() != task.getStatus()) {
      log.debug("Task status changed for task ID: {}. Notifying users.", id);
      task.getContact().getClient().getUsers()
          .forEach(user -> userNotifier.notifyOfTaskStatusChange(user, taskDTO));
    }

    taskMapper.update(task, taskDTO);

    task.setClient(clientRepository.findById(taskDTO.getClient().getId())
        .orElseThrow(() -> {
          log.error("Client not found with ID: {}", taskDTO.getClient().getId());
          return new NotFoundException(ExceptionMessage.NOT_FOUND);
        }));

    task.setContact(contactRepository.findById(taskDTO.getContact().getId())
        .orElseThrow(() -> {
          log.error("Contact not found with ID: {}", taskDTO.getContact().getId());
          return new NotFoundException(ExceptionMessage.NOT_FOUND);
        }));

    return taskMapper.toDto(taskRepository.save(task));
  }

  /**
   * Updates the status of a task and clears the cache. Notifies users of the status change.
   *
   * @param id         The ID of the task to update.
   * @param taskStatus The new status of the task.
   * @return The updated task as TaskDTO.
   */
  @Transactional
  @CacheEvict(value = "tasks", allEntries = true)
  public TaskDTO updateStatus(Long id, TaskStatus taskStatus) {
    log.debug("Updating status for task with ID: {} to {}", id, taskStatus);
    Task task = getTask(id);
    task.setStatus(taskStatus);
    TaskDTO taskDTO = taskMapper.toDto(taskRepository.save(task));
    log.debug("Task status updated. Notifying users.");
    if (task.getContact() != null) {
      task.getContact().getClient().getUsers()
          .forEach(user -> userNotifier.notifyOfTaskStatusChange(user, taskDTO));
    }

    return taskDTO;
  }

  /**
   * Assigns a contact to a task and clears the cache.
   *
   * @param taskId    The ID of the task.
   * @param contactId The ID of the contact to assign.
   * @return The updated task as TaskDTO.
   */
  @Transactional
  @CacheEvict(value = "tasks", allEntries = true)
  public TaskDTO assignContact(Long taskId, Long contactId) {
    log.debug("Assigning contact ID: {} to task ID: {}", contactId, taskId);
    Task task = getTask(taskId);
    Contact contact = contactRepository.findById(contactId)
        .orElseThrow(() -> {
          log.error("Contact not found with ID: {}", contactId);
          return new NotFoundException(ExceptionMessage.NOT_FOUND);
        });
    task.setContact(contact);
    return taskMapper.toDto(taskRepository.save(task));
  }

  /**
   * Checks for tasks that are due soon and notifies users.
   */
  @Scheduled(cron = "0 0 * * * *")
  @Transactional
  public void checkDueDate() {
    log.debug("Checking tasks due within the next hour");
    taskRepository.findTasksDueInTimeRange(LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        .forEach(task -> {
          log.debug("Notifying users for task ID: {} due soon.", task.getId());
          task.getContact().getClient().getUsers()
              .forEach(user -> userNotifier.notifyOfTaskDueDate(user, task));
        });
  }

  /**
   * Pause tasks that have not been executed for a very long time
   */
  @Scheduled(cron = "0 0 0 * * *")
  @Transactional
  public void pauseTasks() {
    log.debug("Checking tasks due within the next hour");
    taskRepository.findTasksDueInTimeRange(LocalDateTime.now(), LocalDateTime.now().plusHours(1))
        .forEach(task -> {
          log.debug("Notifying users for task ID: {} due soon.", task.getId());
          task.getContact().getClient().getUsers()
              .forEach(user -> userNotifier.notifyOfTaskDueDate(user, task));
        });
  }

  /**
   * Deletes a task by ID and clears the cache.
   *
   * @param id The ID of the task to delete.
   */
  @CacheEvict(value = "tasks", allEntries = true)
  public void delete(Long id) {
    log.debug("Deleting task with ID: {}", id);
    taskRepository.deleteById(id);
    log.debug("Task deleted successfully with ID: {}", id);
  }

  /**
   * Retrieves a task entity by ID.
   *
   * @param id The ID of the task to retrieve.
   * @return The task entity.
   * @throws NotFoundException if the task is not found.
   */
  private Task getTask(Long id) {
    log.debug("Fetching task with ID: {}", id);
    return taskRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Task not found with ID: {}", id);
          return new NotFoundException(ExceptionMessage.NOT_FOUND);
        });
  }
}
