package com.sample.crm.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.sample.crm.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Data transfer object representing a task.")
public class TaskDTO implements Serializable {

  @Schema(description = "Unique identifier of the task.", example = "1")
  private Long id;

  @NotBlank(message = "Description cannot be blank")
  @Size(min = 1, max = 255, message = "Description size should be between 1 and 255 symbols")
  @Schema(description = "Description of the task.", example = "Complete project documentation.")
  private String description;

  @NotNull(message = "Status cannot be null")
  @Schema(description = "Current status of the task.", example = "TODO")
  private TaskStatus status;

  @FutureOrPresent(message = "Due date must be in the present or future")
  @Schema(description = "Due date for the task.", example = "2023-12-31")
  private LocalDate dueDate;

  @NotNull(message = "Contact cannot be null")
  @Schema(description = "Contact associated with the task.")
  private ContactDTO contact;

  @NotNull(message = "Client cannot be null")
  @Schema(description = "Client associated with the task.")
  private ClientDTO client;

  @Schema(description = "List of comments associated with the task.")
  private List<CommentDTO> comments;
}
