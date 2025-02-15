package org.grisbi.onefreelance.persistence.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Generic converter for JSONB.
 *
 * @param <T> the entity
 */
@Converter
@RequiredArgsConstructor
@Slf4j
public abstract class JsonConverter<T> implements AttributeConverter<T, String> {
  private final ObjectMapper objectMapper;
  private final Class<T> clazz;

  @Override
  public String convertToDatabaseColumn(T object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException jpe) {
      log.warn("Cannot convert Address into JSON");
      return null;
    }
  }

  @Override
  public T convertToEntityAttribute(String value) {
    try {
      return objectMapper.readValue(value, clazz);
    } catch (JsonProcessingException e) {
      log.warn("Cannot convert JSON into Address");
      return null;
    }
  }
}
