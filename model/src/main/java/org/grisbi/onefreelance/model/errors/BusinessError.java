package org.grisbi.onefreelance.model.errors;

import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This exception is thrown when a business rule is violated.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessError extends RuntimeException {

  /**
   * The functional, unique, error code, used to eases analysis of such exception.
   */
  protected final ErrorHandler errorHandler;

  protected final Option<String> causeMessage;

  /**
   * Business Error constructor.
   */
  public BusinessError(final ErrorHandler errorHandler, final Option<String> maybeMessage, final Throwable cause) {
    super(maybeMessage.map(message -> "%s: %s".formatted(errorHandler.getMessage(), message))
        .getOrElse(errorHandler::getMessage), cause);

    this.errorHandler = errorHandler;
    this.causeMessage = maybeMessage;
  }

  public static BusinessError forError(ErrorHandler errorHandler, String causeMessage) {
    return new BusinessError(errorHandler, Option.some(causeMessage));
  }

  public static BusinessError forError(ErrorHandler errorHandler) {
    return new BusinessError(errorHandler, Option.none());
  }

  @Override
  public String getMessage() {
    return causeMessage.map(cause -> "%s: %s".formatted(errorHandler.getMessage(), cause))
        .getOrElse(errorHandler::getMessage);
  }
}
