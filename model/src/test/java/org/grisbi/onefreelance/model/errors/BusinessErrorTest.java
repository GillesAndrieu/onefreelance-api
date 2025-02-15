package org.grisbi.onefreelance.model.errors;

import static org.assertj.core.api.Assertions.assertThat;

import io.vavr.control.Option;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

class BusinessErrorTest {

  @Property
  void testExceptionWithMessage(@ForAll String message, @ForAll ErrorHandler errorHandler) {
    final var result = BusinessError.forError(errorHandler, message);

    assertThat(result.getCauseMessage().get()).isEqualTo(message);
    assertThat(result.getErrorHandler()).isEqualTo(errorHandler);
  }

  @Property
  void testExceptionWithoutMessage(@ForAll ErrorHandler errorHandler) {
    final var result = BusinessError.forError(errorHandler);

    assertThat(result.getCauseMessage()).isEmpty();
    assertThat(result.getErrorHandler()).isEqualTo(errorHandler);
  }

  @Property
  void testException(@ForAll ErrorHandler errorHandler, @ForAll String message) {
    final var prefixMessage = "Resource not found, make sure the url is correct and the resources exist on database.: ";
    final var result = new BusinessError(errorHandler, Option.of(message), new Exception());

    assertThat(result.getCauseMessage().get()).isEqualTo(message);
    assertThat(result.getErrorHandler()).isEqualTo(errorHandler);
    assertThat(result.getMessage()).isEqualTo(prefixMessage.concat(message));
  }
}
