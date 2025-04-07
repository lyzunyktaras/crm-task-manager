package com.sample.crm.controller;

import com.sample.crm.configuration.db.DynamicRoutingDataSource;
import com.sample.crm.configuration.db.H2HealthChecker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * For testing
 */
@RestController
@RequestMapping("/api/db")
@RequiredArgsConstructor
@Tag(name = "Database Controller", description = "TEST API for switching between the H2 and PostgreSQL databases.")
public class DatabaseController {

  private final DynamicRoutingDataSource dynamicRoutingDataSource;
  private final H2HealthChecker h2HealthChecker;

  @PostMapping("/disable-h2")
  @Operation(summary = "Disable H2", description = "Disables the H2 database, forcing the backup PostgreSQL database to be used.")
  public ResponseEntity<String> disableH2() {
    dynamicRoutingDataSource.setH2Healthy(false);
    h2HealthChecker.setScheduledJobEnabled(false);
    return new ResponseEntity<>("H2 is disabled. Now using a backup PostgreSQL database.", HttpStatus.OK);
  }

  @PostMapping("/enable-h2")
  @Operation(summary = "Enable H2", description = "Enables the H2 database.")
  public ResponseEntity<String> enableH2() {
    dynamicRoutingDataSource.setH2Healthy(true);
    h2HealthChecker.setScheduledJobEnabled(true);
    return new ResponseEntity<>("H2 is back on.", HttpStatus.OK);
  }
}
