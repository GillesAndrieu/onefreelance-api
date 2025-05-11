package org.grisbi.onefreelance.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.grisbi.onefreelance.model.errors.ApiError;
import org.grisbi.onefreelance.model.errors.BusinessError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller exception handler.
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionTranslator {

  /**
   * Exception handler when BusinessError is returned.
   *
   * @param ex business exception
   * @return response error
   */
  @ExceptionHandler(BusinessError.class)
  public ResponseEntity<ApiError> processBusinessError(BusinessError ex) {
    log.warn("Business exception %s".formatted(ex.getMessage()));
    return ResponseEntity.status(ex.getErrorHandler().getStatus())
        .body(ApiError.forException(ex));
  }
}
