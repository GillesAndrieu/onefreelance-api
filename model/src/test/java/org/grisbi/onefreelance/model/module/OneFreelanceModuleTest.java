package org.grisbi.onefreelance.model.module;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.grisbi.onefreelance.model.config.JacksonConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;

@JsonTest
@ContextConfiguration(classes = JacksonConfig.class)
class OneFreelanceModuleTest {

  @Autowired
  private ObjectMapper mapper;

  @SneakyThrows
  @Test
  void bigDecimalSerializer() {

    assertThat(mapper.writeValueAsString(SampleBigDecimal.builder()
        .number(new BigDecimal("20.0"))
        .build())).isEqualTo("{\"number\":20.00}");
    assertThat(mapper.writeValueAsString(SampleBigDecimal.builder()
        .number(new BigDecimal("20"))
        .build())).isEqualTo("{\"number\":20.00}");
    assertThat(mapper.writeValueAsString(SampleBigDecimal.builder()
        .number(new BigDecimal("20.000"))
        .build())).isEqualTo("{\"number\":20.00}");
    assertThat(mapper.writeValueAsString(SampleBigDecimal.builder()
        .number(new BigDecimal("20.5"))
        .build())).isEqualTo("{\"number\":20.50}");
    assertThat(mapper.writeValueAsString(SampleBigDecimal.builder()
        .number(new BigDecimal("20.567"))
        .build())).isEqualTo("{\"number\":20.57}");
    assertThat(mapper.writeValueAsString(SampleBigDecimal.builder()
        .number(new BigDecimal("20.564"))
        .build())).isEqualTo("{\"number\":20.56}");
    assertThat(mapper.writeValueAsString(SampleBigDecimal.builder()
        .number(new BigDecimal("20.565000000"))
        .build())).isEqualTo("{\"number\":20.56}");
    assertThat(mapper.writeValueAsString(SampleBigDecimal.builder()
        .number(new BigDecimal("703632423564.13"))
        .build())).isEqualTo("{\"number\":703632423564.13}");
  }

  @SneakyThrows
  @Test
  void bigDecimalDeserializer() {

    assertThat(mapper.readValue("{\"number\":20.0}", SampleBigDecimal.class))
        .isEqualTo(SampleBigDecimal.builder()
        .number(new BigDecimal("20.00"))
        .build());
    assertThat(mapper.readValue("{\"number\":20}", SampleBigDecimal.class))
        .isEqualTo(SampleBigDecimal.builder()
        .number(new BigDecimal("20.00"))
        .build());
    assertThat(mapper.readValue("{\"number\":20.000}", SampleBigDecimal.class))
        .isEqualTo(SampleBigDecimal.builder()
        .number(new BigDecimal("20.00"))
        .build());
    assertThat(mapper.readValue("{\"number\":20.5}", SampleBigDecimal.class))
        .isEqualTo(SampleBigDecimal.builder()
        .number(new BigDecimal("20.50"))
        .build());
    assertThat(mapper.readValue("{\"number\":20.567}", SampleBigDecimal.class))
        .isEqualTo(SampleBigDecimal.builder()
        .number(new BigDecimal("20.57"))
        .build());
    assertThat(mapper.readValue("{\"number\":20.564}", SampleBigDecimal.class))
        .isEqualTo(SampleBigDecimal.builder()
        .number(new BigDecimal("20.56"))
        .build());
    assertThat(mapper.readValue("{\"number\":20.565000000}", SampleBigDecimal.class))
        .isEqualTo(SampleBigDecimal.builder()
        .number(new BigDecimal("20.56"))
        .build());

  }

  @Value
  @Builder
  @Jacksonized
  private static class SampleBigDecimal {

    BigDecimal number;

  }
}
