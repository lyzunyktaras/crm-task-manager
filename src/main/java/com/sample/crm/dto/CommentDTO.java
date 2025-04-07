package com.sample.crm.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object representing a comment.")
public class CommentDTO implements Serializable {

  @NotNull(message = "ID cannot be null")
  @Schema(description = "Unique identifier of the comment.", example = "1")
  private Long id;

  @NotBlank(message = "Content cannot be blank")
  @Size(max = 500, message = "Content must not exceed 500 characters")
  @Schema(description = "Content of the comment.", example = "This is a sample comment.")
  private String content;

  @NotNull(message = "Sent time cannot be null")
  @Schema(description = "Timestamp when the comment was sent.", example = "2023-04-25T10:15:30")
  private LocalDateTime sentAt;

  @NotNull(message = "Task cannot be null")
  @Schema(description = "Task associated with the comment.")
  private TaskDTO taskDTO;

  @NotBlank(message = "Username cannot be blank")
  @Size(max = 50, message = "Username must not exceed 50 characters")
  @Schema(description = "Username of the commenter.", example = "username")
  private String username;
}
