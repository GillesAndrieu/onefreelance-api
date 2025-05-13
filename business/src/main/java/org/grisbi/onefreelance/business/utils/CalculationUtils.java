package org.grisbi.onefreelance.business.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.grisbi.onefreelance.model.dto.Calculated;
import org.grisbi.onefreelance.model.dto.TaxRateType;
import org.grisbi.onefreelance.model.dto.request.ReportRequest;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;

/**
 * Calculation utils.
 */
@UtilityClass
public class CalculationUtils {

  public static final String VAT_KEY = "vat";
  public static final String TAX_ENTERPRISE_KEY = "tax_enterprise";
  public static final String TAX_CUSTOMER_KEY = "tax_customer";

  /**
   * Calculate total from report.
   *
   * @param contract data
   * @param reportRequest data
   * @param parameters settings
   * @return calculated object
   */
  public Calculated reportCalculate(final ContractEntity contract, final ReportRequest reportRequest,
                                    final Map<String, String> parameters) {

    final TaxRateType taxRateType = contract.getContractData().getTaxRateType();
    final BigDecimal dailyRate = contract.getContractData().getDailyRate();
    final BigDecimal taxRate = (TaxRateType.CURRENCY.equals(taxRateType)
        ? contract.getContractData().getTaxRate()
        : dailyRate.multiply(contract.getContractData().getTaxRate()));

    final BigDecimal totalDay = reportRequest.getActivity().values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

    final BigDecimal contractTaxExcluded = dailyRate.subtract(taxRate);
    final BigDecimal totalTaxExcluded = contractTaxExcluded.multiply(totalDay).add(reportRequest.getBonus())
        .setScale(2, RoundingMode.HALF_UP);
    final BigDecimal vat = totalTaxExcluded.multiply(new BigDecimal(parameters.get(VAT_KEY)))
        .setScale(2, RoundingMode.HALF_UP);
    final BigDecimal totalTaxIncluded = totalTaxExcluded.add(vat).setScale(2, RoundingMode.HALF_UP);
    final BigDecimal totalTaxEnterprise = totalTaxExcluded.multiply(new BigDecimal(parameters.get(TAX_ENTERPRISE_KEY)))
        .setScale(2, RoundingMode.HALF_UP);
    final BigDecimal totalTaxCustomer = totalTaxExcluded.multiply(new BigDecimal(parameters.get(TAX_CUSTOMER_KEY)))
        .setScale(2, RoundingMode.HALF_UP);
    final BigDecimal balance = totalTaxExcluded.subtract(totalTaxEnterprise).subtract(totalTaxCustomer)
        .setScale(2, RoundingMode.HALF_UP);

    return Calculated.builder()
        .totalDay(totalDay)
        .contractTaxExcluded(contractTaxExcluded)
        .totalTaxExcluded(totalTaxExcluded)
        .vat(vat)
        .totalTaxIncluded(totalTaxIncluded)
        .totalTaxEnterprise(totalTaxEnterprise)
        .totalTaxCustomer(totalTaxCustomer)
        .balance(balance)
        .build();
  }
}
