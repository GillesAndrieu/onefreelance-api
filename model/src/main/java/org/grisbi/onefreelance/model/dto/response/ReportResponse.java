package org.grisbi.onefreelance.model.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.grisbi.onefreelance.model.dto.Calculated;

/**
 * The report response DTO.
 */
@Value
@Builder
@Jacksonized
public class ReportResponse {

  UUID id;
  UUID clientId;
  UUID contractId;
  Integer month;
  Integer year;
  Integer billedMonth;
  Integer billedYear;
  Boolean billed;
  Map<String, BigDecimal> activity;
  BigDecimal bonus;
  Calculated calculated;
  Instant createAt;
  Instant updateAt;
}
