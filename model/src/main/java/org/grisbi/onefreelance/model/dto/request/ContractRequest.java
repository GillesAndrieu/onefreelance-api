package org.grisbi.onefreelance.model.dto.request;

import java.math.BigDecimal;
import java.util.Currency;
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

  String name;
  String number;
  BigDecimal dailyRate;
  Currency currencyDailyRate;
  BigDecimal taxRate;
  TaxRateType taxRateType;
}
