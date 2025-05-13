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
import org.grisbi.onefreelance.persistence.entity.ReportEntity.ReportDataEntity;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportDataConverterTest {

  @Mock
  ObjectMapper objectMapperError;

  private final ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  private ReportDataConverter reportConverter;

  @BeforeEach
  void init() {
    this.reportConverter = new ReportDataConverter(objectMapper);
  }

  @Test
  void testConvertToString() {
    final var reportEntity = Instancio.create(ReportDataEntity.class);

    final var converterResult = reportConverter.convertToDatabaseColumn(reportEntity);

    assertThat(converterResult).isNotNull()
        .containsAnyOf(reportEntity.getCustomerId().toString(), reportEntity.getClientId().toString(), reportEntity.getContractId().toString());
  }

  @Test
  void testConvertToString_error() throws JsonProcessingException {
    final var reportDataEntity = ReportDataEntity.builder().build();
    given(objectMapperError.writeValueAsString(any())).willThrow(new JsonProcessingException(""){});

    final var convertError = new ReportDataConverter(objectMapperError);
    assertThrows(IllegalArgumentException.class,
        () -> convertError.convertToDatabaseColumn(reportDataEntity));
  }

  @Test
  void testConvertToEntity() {
    final var converterResult = reportConverter.convertToEntityAttribute("{\"id\":1,\"year\":\"2025\"}");

    assertThat(converterResult).isNotNull();
    assertThat(converterResult.getYear()).isEqualTo(2025);
  }

  @Test
  void testConvertToEntity_error() {
    assertThrows(IllegalArgumentException.class, () -> reportConverter.convertToEntityAttribute("{"));
  }
}
