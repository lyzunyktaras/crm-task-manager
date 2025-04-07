package com.sample.crm.controller;

import java.util.List;

import com.sample.crm.dto.CommentDTO;
import com.sample.crm.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = """
    Manages operations related to comments for tasks.
    It supports retrieving comments for a specific task and adding a new comment to a task.
    The controller integrates with CommentService to perform business logic such as fetching comments, validating input, and saving comments with timestamp and user details.""")
public class CommentController {

  private final CommentService commentService;

  @Operation(
      summary = "Get comments for a task",
      description = """
          Retrieves all comments associated with the specified task.
          The task is identified by its unique ID provided in the path variable.
          Returns a list of CommentDTO objects representing the comments for the task."""
  )
  @ApiResponse(responseCode = "200", description = "List of comments successfully retrieved.")
  @GetMapping("/{id}")
  public ResponseEntity<List<CommentDTO>> getCommentsForTask(
      @Parameter(description = "Unique identifier of the task.", example = "1")
      @PathVariable("id") Long taskId) {
    return new ResponseEntity<>(commentService.getCommentsForTask(taskId), HttpStatus.OK);
  }

  @Operation(
      summary = "Add comment to a task",
      description = """
          Adds a new comment to the specified task.
          The task is identified by its unique ID provided in the path variable.
          The request body must include the comment content and the username of the user posting the comment.
          The comment is timestamped and linked to the task and user.
          Returns the created CommentDTO object.""")
  @ApiResponse(responseCode = "201", description = "Comment successfully added.")
  @PostMapping("/{id}")
  public ResponseEntity<CommentDTO> addCommentToTask(
      @Parameter(description = "Unique identifier of the task to which the comment is to be added.", example = "1")
      @PathVariable("id") Long taskId,
      @Parameter(description = "Comment data for the new comment, including content and the username of the commenting user.", required = true)
      @Valid @RequestBody CommentDTO commentDTO) {
    return new ResponseEntity<>(commentService.saveComment(taskId, commentDTO), HttpStatus.CREATED);
  }
}
