package com.sample.crm.controller;

import com.sample.crm.dto.CommentDTO;
import com.sample.crm.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @Test
    void shouldGetCommentsForTask() {
        Long taskId = 1L;
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setUsername("username");
        commentDTO.setContent("content");
        List<CommentDTO> comments = List.of(commentDTO);
        when(commentService.getCommentsForTask(taskId)).thenReturn(comments);

        ResponseEntity<List<CommentDTO>> response = commentController.getCommentsForTask(taskId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(comments);
        verify(commentService, times(1)).getCommentsForTask(taskId);
    }

    @Test
    void shouldAddCommentToTask() {
        Long taskId = 1L;
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setUsername("username");
        commentDTO.setContent("content");
        when(commentService.saveComment(taskId, commentDTO)).thenReturn(commentDTO);

        ResponseEntity<CommentDTO> response = commentController.addCommentToTask(taskId, commentDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(commentDTO);
        verify(commentService, times(1)).saveComment(taskId, commentDTO);
    }
}
