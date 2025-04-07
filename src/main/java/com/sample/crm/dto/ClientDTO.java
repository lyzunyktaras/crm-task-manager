package com.sample.crm.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Data transfer object representing a client.")
public class ClientDTO implements Serializable {

  @Schema(description = "Unique identifier of the client.", example = "1")
  private Long id;

  @NotBlank
  @Size(min = 1, max = 50, message = "Company name size should be between 1 and 50 symbols")
  @Schema(description = "Name of the company.", example = "Acme Corporation")
  private String companyName;

  @NotBlank
  @Size(min = 1, max = 50, message = "Industry size should be between 1 and 50 symbols")
  @Schema(description = "Industry in which the client operates.", example = "Manufacturing")
  private String industry;

  @NotBlank
  @Size(min = 1, max = 100, message = "Address size should be between 1 and 50 symbols")
  @Schema(description = "Physical address of the client.", example = "123 Main St, Anytown")
  private String address;

  @Schema(description = "List of users associated with the client.")
  private List<UserDTO> users;
}
