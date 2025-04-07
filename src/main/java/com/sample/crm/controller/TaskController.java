package com.sample.crm.controller;

import java.util.List;

import com.sample.crm.dto.TaskDTO;
import com.sample.crm.model.TaskStatus;
import com.sample.crm.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
@Tag(name = "Task Controller", description = """
    Manages operations for task entities.
    It supports retrieving all tasks, fetching tasks by client, creating, updating, and deleting tasks,
    updating task status, and assigning contacts to tasks.
    Additionally, it handles caching and notifies users on status changes or due dates.
    """)
public class TaskController {

  private final TaskService taskService;

  @Operation(
      summary = "Retrieve all tasks",
      description = """
          Fetches a complete list of all task records.
          Uses caching to improve performance by reducing database queries.
          Returns a list of TaskDTO objects.
          """
  )
  @ApiResponse(responseCode = "200", description = "List of all tasks successfully retrieved.")
  @GetMapping
  public ResponseEntity<List<TaskDTO>> findAll() {
    return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
  }

  @Operation(
      summary = "Retrieve tasks by client",
      description = """
          Fetches tasks associated with a specific client.
          The client is identified by its unique ID provided as a path variable.
          Returns a list of TaskDTO objects for the specified client.
          """
  )
  @ApiResponse(responseCode = "200", description = "List of tasks for the specified client successfully retrieved.")
  @GetMapping("/client/{id}")
  public ResponseEntity<List<TaskDTO>> findByClient(
      @Parameter(description = "Unique identifier of the client.", example = "1")
      @PathVariable Long id) {
    return new ResponseEntity<>(taskService.findByClient(id), HttpStatus.OK);
  }

  @Operation(
      summary = "Create a new task",
      description = """
          Creates a new task record using the provided task data.
          The input data is validated, and the created task is returned as a TaskDTO object.
          Caches are cleared to maintain data consistency.
          """
  )
  @ApiResponse(responseCode = "201", description = "Task successfully created.")
  @PostMapping
  public ResponseEntity<TaskDTO> create(
      @Parameter(description = "Task data for the new task, including required fields.", required = true)
      @Valid @RequestBody TaskDTO taskDTO) {
    return new ResponseEntity<>(taskService.create(taskDTO), HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update an existing task",
      description = """
          Updates an existing task identified by the provided ID with the new task data.
          Notifies users if the task status changes.
          Returns the updated TaskDTO object.
          Caches are cleared to ensure updated data is reflected.
          """
  )
  @ApiResponse(responseCode = "200", description = "Task successfully updated.")
  @PutMapping("/{id}")
  public ResponseEntity<TaskDTO> update(
      @Parameter(description = "Unique identifier of the task to be updated.", example = "4")
      @PathVariable Long id,
      @Parameter(description = "Updated task data with valid fields to modify the task record.", required = true)
      @Valid @RequestBody TaskDTO taskDTO) {
    return new ResponseEntity<>(taskService.update(id, taskDTO), HttpStatus.OK);
  }

  @Operation(
      summary = "Delete a task",
      description = """
          Deletes the task identified by the provided ID.
          Returns a 204 No Content status indicating the task has been successfully removed.
          Caches are cleared to maintain data consistency.
          """
  )
  @ApiResponse(responseCode = "204", description = "Task successfully deleted.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "Unique identifier of the task to be deleted.", example = "4")
      @PathVariable Long id) {
    taskService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(
      summary = "Update task status",
      description = """
          Updates the status of a task identified by its unique ID.
          The new status is provided as a request parameter.
          Notifies users of the status change.
          Returns the updated TaskDTO object.
          Caches are cleared to ensure updated data is reflected.
          """
  )
  @ApiResponse(responseCode = "200", description = "Task status successfully updated.")
  @PatchMapping("/status/{id}")
  public ResponseEntity<TaskDTO> updateStatus(
      @Parameter(description = "Unique identifier of the task to update.", example = "4")
      @PathVariable Long id,
      @Parameter(description = "New status of the task.", example = "IN_PROGRESS", required = true)
      @RequestParam("status") TaskStatus taskStatus) {
    return new ResponseEntity<>(taskService.updateStatus(id, taskStatus), HttpStatus.OK);
  }

  @Operation(
      summary = "Assign a contact to a task",
      description = """
          Assigns a contact to a task.
          The task is identified by its unique ID and the contact by the provided contact ID.
          Returns the updated TaskDTO object.
          Caches are cleared to maintain data consistency.
          """
  )
  @ApiResponse(responseCode = "200", description = "Contact successfully assigned to the task.")
  @PatchMapping("/assign/{id}")
  public ResponseEntity<TaskDTO> assignContact(
      @Parameter(description = "Unique identifier of the task.", example = "4")
      @PathVariable("id") Long taskId,
      @Parameter(description = "Unique identifier of the contact to be assigned.", example = "2")
      @RequestParam("contact") Long contactId) {
    return new ResponseEntity<>(taskService.assignContact(taskId, contactId), HttpStatus.OK);
  }
}
