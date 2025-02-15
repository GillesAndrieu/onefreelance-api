package org.grisbi.onefreelance.persistence.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
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
    return Try.of(() -> objectMapper.writeValueAsString(object))
        .getOrElseThrow(() -> new IllegalArgumentException("Unable to convert object to JSON: " + object));
  }

  @Override
  public T convertToEntityAttribute(String value) {
    return Try.of(() -> objectMapper.readValue(value, clazz))
        .getOrElseThrow(() -> new IllegalArgumentException("Cannot convert " + value + " into " + clazz.getName()));
  }
}
