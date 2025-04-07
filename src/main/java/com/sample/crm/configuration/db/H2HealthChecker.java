package com.sample.crm.configuration.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class H2HealthChecker {

  private final DataSource h2DataSource;
  private final DynamicRoutingDataSource dynamicRoutingDataSource;

  @Getter
  @Setter
  private volatile boolean scheduledJobEnabled = true;

  public H2HealthChecker(@Qualifier("h2DataSource") DataSource h2DataSource,
                         DynamicRoutingDataSource dynamicRoutingDataSource) {
    this.h2DataSource = h2DataSource;
    this.dynamicRoutingDataSource = dynamicRoutingDataSource;
  }

  @Scheduled(fixedDelay = 5000)
  public void checkH2Health() {
    if (!scheduledJobEnabled) {
      return;
    }
    try (Connection conn = h2DataSource.getConnection()) {
      if (conn.isValid(1)) {
        dynamicRoutingDataSource.setH2Healthy(true);
        log.debug("H2 healthy");
      }
    } catch (SQLException ex) {
      dynamicRoutingDataSource.setH2Healthy(false);
      log.error("H2 is unavailable, switching to PostgreSQL: ", ex);
    }
  }
}
