package org.grisbi.onefreelance.model.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * Define the ApiError returned by controller.
 */
@Value
@Builder
@Jacksonized
@Getter
public class ApiError {

  @Schema(description = "The moment at which the error happened.", example = "2030-08-25T10:42:59", format = "ISO 8601")
  @Default
  Instant timestamp = Instant.now();

  @Schema(description = "The error code to interpret.", example = "AUTHORIZE_NOT_ALLOWED")
  ErrorHandler error;
  @Schema(description = "The message explaining the cause of the error.",
      example = "Authorize action not callable, it can only be impersonated through status or verify action.")
  String errorDescription;

  @Schema(description = "The HTTP status returned by this HTTP response.", example = "409")
  int status;

  /**
   * The ApiError for BusinessError exception.
   *
   * @param e business exception
   * @return ApiError
   */
  public static ApiError forException(BusinessError e) {
    return ApiError.builder()
        .error(e.getErrorHandler())
        .errorDescription(e.getMessage())
        .status(e.getErrorHandler().getStatus().value())
        .build();
  }

  /**
   * Generic Error handler.
   *
   * @param code error
   * @return ApiError
   */
  public static ApiError forCode(ErrorHandler code) {
    return ApiError.builder()
        .error(code)
        .errorDescription(code.getMessage())
        .status(code.getStatus().value())
        .build();
  }

  /**
   * Generic error handler with message.
   *
   * @param code    error
   * @param message of error
   * @return the api error
   */
  public static ApiError forCodeAndMessage(ErrorHandler code, String message) {
    return ApiError.builder()
        .error(code)
        .errorDescription(message)
        .status(code.getStatus().value())
        .build();
  }

}
