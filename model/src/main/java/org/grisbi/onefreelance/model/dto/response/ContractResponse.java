package org.grisbi.onefreelance.model.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.grisbi.onefreelance.model.dto.TaxRateType;

/**
 * The contract response DTO.
 */
@Value
@Builder
@Jacksonized
public class ContractResponse {

  UUID id;
  UUID clientId;
  String name;
  String number;
  BigDecimal dailyRate;
  Currency currencyDailyRate;
  BigDecimal taxRate;
  TaxRateType taxRateType;
  Instant createAt;
  Instant updateAt;
}
