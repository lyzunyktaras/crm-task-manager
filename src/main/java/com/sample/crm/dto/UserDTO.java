package com.sample.crm.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Data transfer object representing a user.")
public class UserDTO implements Serializable {

  @Schema(description = "Unique identifier of the user.", example = "1")
  private Long id;

  @NotBlank(message = "Username must not be blank")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  @Schema(description = "Username of the user.", example = "username")
  private String username;

  @NotBlank(message = "Password must not be blank")
  @Size(min = 3, max = 100, message = "Password must be between 8 and 100 characters")
  @Schema(description = "Password of the user.", example = "P@ssw0rd!")
  private String password;
}
