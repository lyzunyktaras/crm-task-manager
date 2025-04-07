package com.sample.crm.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO representing task statistics including total tasks and counts by task status.")
public class TaskStatisticsDTO {

  @Schema(description = "Total number of tasks.", example = "200")
  private Long totalTasks;

  @Schema(description = "Number of tasks with status 'open'.", example = "80")
  private Long openTasks;

  @Schema(description = "Number of tasks with status 'in progress'.", example = "60")
  private Long inProgressTasks;

  @Schema(description = "Number of tasks with status 'completed'.", example = "50")
  private Long completedTasks;

  @Schema(description = "Number of tasks that are overdue.", example = "10")
  private Long overdueTasks;
}
