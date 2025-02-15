package org.grisbi.onefreelance.model.errors;

import static org.assertj.core.api.Assertions.assertThat;

import io.vavr.control.Option;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

class ApiErrorTest {

  @Property
  void testForCode(@ForAll ErrorHandler errorHandler) {
    final var result = ApiError.forCode(errorHandler);
    assertThat(result.getError()).isEqualTo(errorHandler);
    assertThat(result.getErrorDescription()).isEqualTo(errorHandler.getMessage());
    assertThat(result.getStatus()).isEqualTo(errorHandler.getStatus().value());
  }

  @Property
  void forException(@ForAll ErrorHandler errorHandler, @ForAll String message) {
    final var result = ApiError.forException(new BusinessError(errorHandler, Option.of(message)));
    assertThat(result.getError()).isEqualTo(errorHandler);
    assertThat(result.getErrorDescription()).contains(message)
        .contains(errorHandler.getMessage());
    assertThat(result.getStatus()).isEqualTo(errorHandler.getStatus().value());
  }

  @Property
  void forCodeAndMessage(@ForAll ErrorHandler errorHandler, @ForAll String message) {
    final var result = ApiError.forCodeAndMessage(errorHandler, message);
    assertThat(result.getError()).isEqualTo(errorHandler);
    assertThat(result.getErrorDescription()).isEqualTo(message);
    assertThat(result.getStatus()).isEqualTo(errorHandler.getStatus().value());
  }
}
