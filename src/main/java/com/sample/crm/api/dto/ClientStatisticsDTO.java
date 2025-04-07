package com.sample.crm.api.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO representing client statistics including total number of clients and distribution by industry.")
public class ClientStatisticsDTO {

  @Schema(description = "Total number of clients.", example = "100")
  private Long totalClients;

  @Schema(description = "Mapping of industry names to the count of clients in each industry.", example = "{\"Manufacturing\": 50, \"Retail\": 30, \"Technology\": 20}")
  private Map<String, Long> clientsByIndustry;
}
