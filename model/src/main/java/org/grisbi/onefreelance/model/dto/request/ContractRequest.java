package org.grisbi.onefreelance.model.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.grisbi.onefreelance.model.dto.TaxRateType;

/**
 * The contract request DTO.
 */
@Value
@Builder
@Jacksonized
public class ContractRequest {

  @NotNull
  UUID clientId;
  @NotNull
  String name;
  @NotNull
  String number;
  @NotNull
  BigDecimal dailyRate;
  @NotNull
  Currency currencyDailyRate;
  @NotNull
  BigDecimal taxRate;
  @Builder.Default
  TaxRateType taxRateType = TaxRateType.CURRENCY;
}
