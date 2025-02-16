package org.grisbi.onefreelance.model.module;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.math.BigDecimal;
import lombok.SneakyThrows;

/**
 * Module BigDecimal serializer.
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

  @Override
  @SneakyThrows
  public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
    jsonGenerator.writeNumber(Formats.AMOUNT_FORMAT.format(value));
  }
}