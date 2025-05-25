package org.grisbi.onefreelance.business.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.grisbi.onefreelance.business.utils.CalculationUtils.TAX_COMPANY_KEY;
import static org.grisbi.onefreelance.business.utils.CalculationUtils.TAX_CUSTOMER_KEY;
import static org.grisbi.onefreelance.business.utils.CalculationUtils.VAT_KEY;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import org.grisbi.onefreelance.model.dto.Calculated;
import org.grisbi.onefreelance.model.dto.TaxRateType;
import org.grisbi.onefreelance.model.dto.request.ReportRequest;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculationUtilsTest {

  @Test
  void given_report_when_call_reportCalculate_returns_correct_result() {
    final var contract = getCurrencyContract();
    final var report = getReport();
    final var parameters = configMap();

    final Calculated calculate = CalculationUtils.reportCalculate(contract, report, parameters);

    assertThat(calculate.getTotalDay()).isEqualTo(new BigDecimal("6"));
    assertThat(calculate.getContractTaxExcluded()).isEqualTo(new BigDecimal("485"));
    assertThat(calculate.getTotalTaxExcluded()).isEqualTo(new BigDecimal("3310.00"));
    assertThat(calculate.getTotalTaxIncluded()).isEqualTo(new BigDecimal("3972.00"));
    assertThat(calculate.getVat()).isEqualTo(new BigDecimal("662.00"));
    assertThat(calculate.getTotalTaxCompany()).isEqualTo(new BigDecimal("817.57"));
    assertThat(calculate.getTotalTaxCustomer()).isEqualTo(new BigDecimal("463.40"));
    assertThat(calculate.getBalance()).isEqualTo(new BigDecimal("2029.03"));
  }

  @Test
  void given_report_and_percentage_contract_when_call_reportCalculate_returns_correct_result() {
    final var contract = getPercentageContract();
    final var report = getReport();
    final var parameters = configMap();

    final Calculated calculate = CalculationUtils.reportCalculate(contract, report, parameters);

    assertThat(calculate.getTotalDay()).isEqualTo(new BigDecimal("6"));
    assertThat(calculate.getContractTaxExcluded()).isEqualTo(new BigDecimal("425.00"));
    assertThat(calculate.getTotalTaxExcluded()).isEqualTo(new BigDecimal("2950.00"));
    assertThat(calculate.getTotalTaxIncluded()).isEqualTo(new BigDecimal("3540.00"));
    assertThat(calculate.getVat()).isEqualTo(new BigDecimal("590.00"));
    assertThat(calculate.getTotalTaxCompany()).isEqualTo(new BigDecimal("728.65"));
    assertThat(calculate.getTotalTaxCustomer()).isEqualTo(new BigDecimal("413.00"));
    assertThat(calculate.getBalance()).isEqualTo(new BigDecimal("1808.35"));
  }

  private ReportRequest getReport() {
    return ReportRequest.builder()
        .activity(Map.of("1", new BigDecimal("1"),
            "2", new BigDecimal("1"),
            "3", new BigDecimal("1"),
            "4", new BigDecimal("1"),
            "5", new BigDecimal("1"),
            "6", new BigDecimal("0"),
            "7", new BigDecimal("0"),
            "8", new BigDecimal("1")))
        .bonus(new BigDecimal("400"))
        .build();
  }

  private ContractEntity getCurrencyContract() {
    return ContractEntity.builder()
        .contractData(ContractEntity.ContractDataEntity.builder()
            .currencyDailyRate(Currency.getInstance("EUR"))
            .taxRate(new BigDecimal("15"))
            .dailyRate(new BigDecimal("500"))
            .taxRateType(TaxRateType.CURRENCY)
            .build())
        .build();
  }

  private ContractEntity getPercentageContract() {
    return ContractEntity.builder()
        .contractData(ContractEntity.ContractDataEntity.builder()
            .currencyDailyRate(Currency.getInstance("EUR"))
            .taxRate(new BigDecimal("15"))
            .dailyRate(new BigDecimal("500"))
            .taxRateType(TaxRateType.PERCENTAGE)
            .build())
        .build();
  }

  private Map<String, String> configMap() {
    return Map.of(VAT_KEY, "0.2",
        TAX_CUSTOMER_KEY, "0.14",
        TAX_COMPANY_KEY, "0.247");
  }
}
