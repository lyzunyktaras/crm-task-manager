package com.sample.crm.api.controller;

import com.sample.crm.api.dto.ClientStatisticsDTO;
import com.sample.crm.api.dto.TaskStatisticsDTO;
import com.sample.crm.api.service.ClientStatisticsService;
import com.sample.crm.api.service.TaskStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
@Tag(name = "Statistics Controller", description = """
    Provides endpoints for retrieving statistical data.
    The /clients endpoint returns overall statistics about clients including total clients count 
    and distribution by industry.
    The /tasks endpoint returns overall task statistics such as total tasks, counts by status 
    (open, in progress, completed, overdue).
    """)
public class StatisticsController {

  private final ClientStatisticsService clientStatisticsService;
  private final TaskStatisticsService taskStatisticsService;

  @Operation(
      summary = "Get client statistics",
      description = """
          Retrieves statistical data about clients.
          Returns a ClientStatisticsDTO containing the total number of clients and a breakdown by industry.
          """
  )
  @ApiResponse(responseCode = "200", description = "Client statistics successfully retrieved.")
  @GetMapping("/clients")
  public ResponseEntity<ClientStatisticsDTO> getClientStatistics() {
    return new ResponseEntity<>(clientStatisticsService.getClientStatistics(), HttpStatus.OK);
  }

  @Operation(
      summary = "Get task statistics",
      description = """
          Retrieves statistical data about tasks.
          Returns a TaskStatisticsDTO containing total tasks, counts by status (open, in progress, completed, overdue).
          """
  )
  @ApiResponse(responseCode = "200", description = "Task statistics successfully retrieved.")
  @GetMapping("/tasks")
  public ResponseEntity<TaskStatisticsDTO> getTaskStatistics() {
    return new ResponseEntity<>(taskStatisticsService.getTaskStatistics(), HttpStatus.OK);
  }
}
