package com.sample.crm.configuration.db;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class DataSourceConfig {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.h2")
  public DataSource h2DataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.postgres")
  public DataSource postgresDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  public DynamicRoutingDataSource routingDataSource(
      @Qualifier("h2DataSource") DataSource h2DataSource,
      @Qualifier("postgresDataSource") DataSource postgresDataSource) {

    Map<Object, Object> targetDataSources = new HashMap<>();
    targetDataSources.put("H2", h2DataSource);
    targetDataSources.put("PG", postgresDataSource);

    DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
    routingDataSource.setTargetDataSources(targetDataSources);
    routingDataSource.setDefaultTargetDataSource(h2DataSource);
    routingDataSource.afterPropertiesSet();
    return routingDataSource;
  }
}
