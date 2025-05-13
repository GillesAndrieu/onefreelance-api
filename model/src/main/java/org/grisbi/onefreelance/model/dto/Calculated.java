package org.grisbi.onefreelance.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Caclulated data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Calculated implements Serializable {

  private BigDecimal totalDay;
  private BigDecimal contractTaxExcluded;
  private BigDecimal totalTaxExcluded;
  private BigDecimal totalTaxIncluded;
  private BigDecimal vat;
  private BigDecimal totalTaxEnterprise;
  private BigDecimal totalTaxCustomer;
  private BigDecimal balance;
}
