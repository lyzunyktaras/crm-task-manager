package com.sample.crm.configuration.db;

import lombok.Setter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Setter
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
  private volatile boolean h2Healthy = true;

  @Override
  protected Object determineCurrentLookupKey() {
    return h2Healthy ? "H2" : "PG";
  }
}