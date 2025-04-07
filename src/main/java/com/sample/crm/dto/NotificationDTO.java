package com.sample.crm.dto;

import java.io.Serializable;
import java.util.Map;

import com.sample.crm.model.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object representing a notification.")
public class NotificationDTO implements Serializable {

  @Schema(description = "Unique identifier of the notification.", example = "1")
  private Long id;

  @NotNull(message = "Notification type must not be null")
  @Schema(description = "Type of the notification.", example = "TASK_STATUS_CHANGED")
  private NotificationType type;

  @Schema(description = "Timestamp when the notification was sent.", example = "2023-04-25T10:15:30")
  private String sentAt;

  @Schema(description = "Indicates whether the notification has been dismissed.", example = "false")
  private boolean dismissed;

  @Schema(description = "Indicates whether the notification has been viewed.", example = "false")
  private boolean viewed;

  @Schema(description = "Additional parameters for the notification as key-value pairs.")
  private Map<String, String> params;
}
