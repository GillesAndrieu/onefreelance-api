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
import org.grisbi.onefreelance.persistence.entity.ContractEntity.ContractDataEntity;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContractDataConverterTest {

  @Mock
  ObjectMapper objectMapperError;

  private final ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  private ContractDataConverter contractConverter;

  @BeforeEach
  void init() {
    this.contractConverter = new ContractDataConverter(objectMapper);
  }

  @Test
  void testConvertToString() {
    final var contractEntity = Instancio.create(ContractDataEntity.class);

    final var converterResult = contractConverter.convertToDatabaseColumn(contractEntity);

    assertThat(converterResult).isNotNull()
        .containsAnyOf(contractEntity.getName(), contractEntity.getNumber(), contractEntity.getTaxRateType().name());
  }

  @Test
  void testConvertToString_error() throws JsonProcessingException {
    final var contractDataEntity = ContractDataEntity.builder().build();
    given(objectMapperError.writeValueAsString(any())).willThrow(new JsonProcessingException(""){});

    final var convertError = new ContractDataConverter(objectMapperError);
    assertThrows(IllegalArgumentException.class,
        () -> convertError.convertToDatabaseColumn(contractDataEntity));
  }

  @Test
  void testConvertToEntity() {
    final var converterResult = contractConverter.convertToEntityAttribute("{\"id\":1,\"name\":\"contract name\"}");

    assertThat(converterResult).isNotNull();
    assertThat(converterResult.getName()).isEqualTo("contract name");
  }

  @Test
  void testConvertToEntity_error() {
    assertThrows(IllegalArgumentException.class, () -> contractConverter.convertToEntityAttribute("{"));
  }
}
