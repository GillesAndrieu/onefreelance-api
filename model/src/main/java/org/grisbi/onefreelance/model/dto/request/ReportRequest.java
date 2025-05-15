package org.grisbi.onefreelance.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * The client request DTO.
 */
@Value
@Builder
@Jacksonized
public class ReportRequest {

  @NotNull
  UUID clientId;
  @NotNull
  UUID contractId;
  @NotNull
  @Positive
  Integer month;
  @NotNull
  @Positive
  Integer year;
  @Positive
  Integer billedMonth;
  @Positive
  Integer billedYear;
  @Builder.Default
  Boolean billed = Boolean.FALSE;
  @NotNull
  @Size(min = 28, max = 31)
  Map<String, BigDecimal> activity;
  @Builder.Default
  BigDecimal bonus = BigDecimal.ZERO;
}
