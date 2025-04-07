package com.sample.crm.service;

import com.sample.crm.dto.CommentDTO;
import com.sample.crm.entity.Comment;
import com.sample.crm.entity.Task;
import com.sample.crm.entity.User;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.mapper.CommentMapper;
import com.sample.crm.mapper.TaskMapper;
import com.sample.crm.repository.CommentRepository;
import com.sample.crm.repository.TaskRepository;
import com.sample.crm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private CommentService commentService;

    private Task task;
    private Comment comment;
    private User user;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setDescription("Test Task");

        user = new User();
        user.setUsername("testUser");

        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test Comment");
        comment.setSentAt(LocalDateTime.now());
        comment.setTask(task);
        comment.setUser(user);
    }

    @Test
    void shouldGetCommentsForTask() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent(comment.getContent());
        commentDTO.setUsername(user.getUsername());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(commentRepository.findByTask(task)).thenReturn(List.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(commentDTO);
        when(taskMapper.toDto(task)).thenReturn(null);

        List<CommentDTO> result = commentService.getCommentsForTask(task.getId());

        assertEquals(1, result.size());
        assertEquals(comment.getContent(), result.get(0).getContent());
        assertEquals(user.getUsername(), result.get(0).getUsername());
        verify(taskRepository, times(1)).findById(task.getId());
        verify(commentRepository, times(1)).findByTask(task);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundInGetComments() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> commentService.getCommentsForTask(task.getId()));

        assertEquals("NOT_FOUND", exception.getMessage());
        verify(taskRepository, times(1)).findById(task.getId());
        verify(commentRepository, never()).findByTask(any());
    }

    @Test
    void shouldSaveCommentForTask() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("New Comment");
        commentDTO.setUsername(user.getUsername());

        Comment newComment = new Comment();
        newComment.setContent(commentDTO.getContent());
        newComment.setSentAt(LocalDateTime.now());
        newComment.setTask(task);
        newComment.setUser(user);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(newComment);
        when(commentMapper.toDto(newComment)).thenReturn(commentDTO);

        CommentDTO result = commentService.saveComment(task.getId(), commentDTO);

        assertEquals(commentDTO.getContent(), result.getContent());
        assertEquals(commentDTO.getUsername(), result.getUsername());
        verify(taskRepository, times(1)).findById(task.getId());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFoundInSaveComment() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUsername(user.getUsername());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> commentService.saveComment(task.getId(), commentDTO));

        assertEquals("NOT_FOUND", exception.getMessage());
        verify(taskRepository, times(1)).findById(task.getId());
        verify(userRepository, never()).findByUsername(anyString());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundInSaveComment() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUsername(user.getUsername());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> commentService.saveComment(task.getId(), commentDTO));

        assertEquals("NOT_FOUND", exception.getMessage());
        verify(taskRepository, times(1)).findById(task.getId());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(commentRepository, never()).save(any());
    }
}
