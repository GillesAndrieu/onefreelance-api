package org.grisbi.onefreelance.model.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Class defined to manage all exceptions response and httpStatus.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorHandler {

  @Schema(description = "Returned when the resource is not found.")
  NOT_FOUND("Resource not found, make sure the url is correct and the resources exist on database.",
      HttpStatus.NOT_FOUND);

  private final String message;
  private final HttpStatus status;
}
