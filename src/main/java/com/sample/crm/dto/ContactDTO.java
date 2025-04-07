package com.sample.crm.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Data transfer object representing a contact.")
public class ContactDTO implements Serializable {

  @Schema(description = "Unique identifier of the contact.", example = "1")
  private Long id;

  @NotBlank(message = "First name cannot be blank")
  @Size(min = 1, max = 50, message = "First name size should be between 1 and 50 symbols")
  @Schema(description = "First name of the contact.", example = "Name")
  private String firstName;

  @NotBlank(message = "Last name cannot be blank")
  @Size(min = 1, max = 50, message = "Last name size should be between 1 and 50 symbols")
  @Schema(description = "Last name of the contact.", example = "LastName")
  private String lastName;

  @NotBlank(message = "Email cannot be blank")
  @Email(message = "Invalid email format")
  @Size(max = 50, message = "Email size should not exceed 50 symbols")
  @Schema(description = "Email address of the contact.", example = "email@example.com")
  private String email;

  @NotBlank(message = "Phone number cannot be blank")
  @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid and contain 10-15 digits")
  @Schema(description = "Phone number of the contact.", example = "+12345678901")
  private String phoneNumber;

  @NotNull(message = "Client cannot be null")
  @Schema(description = "Client associated with the contact.")
  private ClientDTO client;
}
