package org.grisbi.onefreelance.model.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import java.math.BigDecimal;

/**
 * OneFreelance module.
 */
public class OneFreelanceModule extends SimpleModule {

  public OneFreelanceModule() {
    this.addSerializer(BigDecimal.class, new BigDecimalSerializer());
    this.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
  }
}
