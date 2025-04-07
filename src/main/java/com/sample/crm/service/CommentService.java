package com.sample.crm.service;

import java.time.LocalDateTime;
import java.util.List;

import com.sample.crm.dto.CommentDTO;
import com.sample.crm.entity.Comment;
import com.sample.crm.entity.Task;
import com.sample.crm.entity.User;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.exception.model.ExceptionMessage;
import com.sample.crm.mapper.CommentMapper;
import com.sample.crm.mapper.TaskMapper;
import com.sample.crm.repository.CommentRepository;
import com.sample.crm.repository.TaskRepository;
import com.sample.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  private final UserRepository userRepository;

  /**
   * Retrieves all comments for a specific task.
   *
   * @param taskId The ID of the task.
   * @return List of comments as CommentDTO.
   */
  @Transactional(readOnly = true)
  public List<CommentDTO> getCommentsForTask(Long taskId) {
    log.debug("Fetching comments for task with ID: {}", taskId);
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> {
          log.error("Task not found with ID: {}", taskId);
          return new NotFoundException(ExceptionMessage.NOT_FOUND);
        });

    return commentRepository.findByTask(task).stream()
        .map(comment -> {
          CommentDTO commentDTO = commentMapper.toDto(comment);
          commentDTO.setTaskDTO(taskMapper.toDto(task));
          commentDTO.setUsername(comment.getUser().getUsername());
          return commentDTO;
        })
        .toList();
  }

  /**
   * Saves a comment for a specific task.
   *
   * @param taskId     The ID of the task to which the comment belongs.
   * @param commentDTO The comment data to save.
   * @return The saved comment as CommentDTO.
   */
  @Transactional
  public CommentDTO saveComment(Long taskId, CommentDTO commentDTO) {
    log.debug("Saving comment for task with ID: {}", taskId);
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> {
          log.error("Task not found with ID: {}", taskId);
          return new NotFoundException(ExceptionMessage.NOT_FOUND);
        });

    User user = userRepository.findByUsername(commentDTO.getUsername())
        .orElseThrow(() -> {
          log.error("User not found with username: {}", commentDTO.getUsername());
          return new NotFoundException(ExceptionMessage.NOT_FOUND);
        });

    Comment comment = new Comment();
    comment.setContent(commentDTO.getContent());
    comment.setSentAt(LocalDateTime.now());
    comment.setTask(task);
    comment.setUser(user);

    return commentMapper.toDto(commentRepository.save(comment));
  }
}
