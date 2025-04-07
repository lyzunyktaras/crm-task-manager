package com.sample.crm;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@Slf4j
@SpringBootApplication
public class CrmApplication implements CommandLineRunner {
  private final DataSource dataSource;

  public CrmApplication(@Qualifier("h2DataSource") DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public static void main(String[] args) {
    SpringApplication.run(CrmApplication.class, args);
  }

  @Override
  public void run(String... args) throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("init_h2.sql"));
      log.info("init_h2.sql successfully executed");
    }
  }
}
