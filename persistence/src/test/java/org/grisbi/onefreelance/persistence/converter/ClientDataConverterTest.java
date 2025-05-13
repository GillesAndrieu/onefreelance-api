package org.grisbi.onefreelance.persistence.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.grisbi.onefreelance.persistence.entity.ClientEntity.ClientDataEntity;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientDataConverterTest {

  @Mock
  ObjectMapper objectMapperError;

  private final ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  private ClientDataConverter clientConverter;

  @BeforeEach
  void init() {
    this.clientConverter = new ClientDataConverter(objectMapper);
  }

  @Test
  void testConvertToString() {
    final var clientEntity = Instancio.create(ClientDataEntity.class);

    final var converterResult = clientConverter.convertToDatabaseColumn(clientEntity);

    assertThat(converterResult).isNotNull()
        .containsAnyOf(clientEntity.getName(), clientEntity.getReferent(), clientEntity.getSiret());
  }

  @Test
  void testConvertToString_error() throws JsonProcessingException {
    final var clientDataEntity = ClientDataEntity.builder().build();
    given(objectMapperError.writeValueAsString(any())).willThrow(new JsonProcessingException(""){});

    final var convertError = new ClientDataConverter(objectMapperError);
    assertThrows(IllegalArgumentException.class,
        () -> convertError.convertToDatabaseColumn(clientDataEntity));
  }

  @Test
  void testConvertToEntity() {
    final var converterResult = clientConverter.convertToEntityAttribute("{\"id\":1,\"name\":\"client name\"}");

    assertThat(converterResult).isNotNull();
    assertThat(converterResult.getName()).isEqualTo("client name");
  }

  @Test
  void testConvertToEntity_error() {
    assertThrows(IllegalArgumentException.class, () -> clientConverter.convertToEntityAttribute("{"));
  }
}
