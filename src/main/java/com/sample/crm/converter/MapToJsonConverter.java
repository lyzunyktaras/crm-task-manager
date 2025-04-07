package com.sample.crm.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringEscapeUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = true)
public class MapToJsonConverter implements AttributeConverter<Map<String, String>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Map<String, String> attribute) {
    if (attribute == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting map to JSON string", e);
    }
  }

  @Override
  public Map<String, String> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return new HashMap<>();
    }
    if (dbData.startsWith("\"") && dbData.endsWith("\"")) {
      dbData = dbData.substring(1, dbData.length() - 1);
    }
    dbData = StringEscapeUtils.unescapeJson(dbData);
    try {
      return objectMapper.readValue(dbData, new TypeReference<>() {
      });
    } catch (IOException e) {
      throw new IllegalArgumentException("Error converting JSON string to map", e);
    }
  }
}
