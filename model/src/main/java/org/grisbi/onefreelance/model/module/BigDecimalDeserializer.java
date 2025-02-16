package org.grisbi.onefreelance.model.module;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.math.BigDecimal;
import lombok.SneakyThrows;

/**
 * Module BigDecimal deserializer.
 */
public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

  @Override
  @SneakyThrows
  public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) {
    return new BigDecimal(Formats.AMOUNT_FORMAT.format(new BigDecimal(jp.getValueAsString())));
  }

}
