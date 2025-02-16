package org.grisbi.onefreelance.model.module;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Module BigDecimal deserializer.
 */
public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

  @Override
  public BigDecimal deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
    return new BigDecimal(Formats.AMOUNT_FORMAT.format(new BigDecimal(jp.getValueAsString())));
  }

}
