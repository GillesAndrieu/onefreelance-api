package org.grisbi.onefreelance.persistence.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.grisbi.onefreelance.persistence.entity.CustomerEntity.CustomerDataEntity;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerDataConverterTest {

  @Mock
  ObjectMapper objectMapperError;

  private final ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  private CustomerDataConverter profileConverter;

  @BeforeEach
  void init() {
    this.profileConverter = new CustomerDataConverter(objectMapper);
  }

  @Test
  void testConvertToString() {
    final var profileEntity = Instancio.create(CustomerDataEntity.class);

    final var converterResult = profileConverter.convertToDatabaseColumn(profileEntity);

    assertThat(converterResult).isNotNull()
        .containsAnyOf(profileEntity.getFirstname(), profileEntity.getLastname(), profileEntity.getEmail());
  }

  @Test
  void testConvertToString_error() throws JsonProcessingException {
    final var profileEntity = CustomerDataEntity.builder().build();
    given(objectMapperError.writeValueAsString(any())).willThrow(new JsonProcessingException(""){});

    final var convertError = new CustomerDataConverter(objectMapperError);
    assertThrows(IllegalArgumentException.class,
        () -> convertError.convertToDatabaseColumn(profileEntity));
  }

  @Test
  void testConvertToEntity() {
    final var converterResult = profileConverter.convertToEntityAttribute("{\"id\":1,\"firstname\":\"John\"}");

    assertThat(converterResult).isNotNull();
    assertThat(converterResult.getFirstname()).isEqualTo("John");
  }

  @Test
  void testConvertToEntity_error() {
    assertThrows(IllegalArgumentException.class, () -> profileConverter.convertToEntityAttribute("{"));
  }
}
