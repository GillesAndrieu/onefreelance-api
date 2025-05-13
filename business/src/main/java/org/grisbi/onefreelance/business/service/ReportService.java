package org.grisbi.onefreelance.business.service;

import static org.grisbi.onefreelance.business.utils.CalculationUtils.TAX_CUSTOMER_KEY;
import static org.grisbi.onefreelance.business.utils.CalculationUtils.TAX_ENTERPRISE_KEY;
import static org.grisbi.onefreelance.business.utils.CalculationUtils.VAT_KEY;

import io.vavr.control.Try;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grisbi.onefreelance.business.mappers.ReportMapper;
import org.grisbi.onefreelance.business.utils.CalculationUtils;
import org.grisbi.onefreelance.business.utils.UserUtils;
import org.grisbi.onefreelance.model.dto.Calculated;
import org.grisbi.onefreelance.model.dto.request.ReportRequest;
import org.grisbi.onefreelance.model.dto.response.ReportResponse;
import org.grisbi.onefreelance.model.errors.BusinessError;
import org.grisbi.onefreelance.model.errors.ErrorHandler;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;
import org.grisbi.onefreelance.persistence.entity.ReportEntity;
import org.grisbi.onefreelance.persistence.repository.ContractRepository;
import org.grisbi.onefreelance.persistence.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Report Service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

  @Value("${spring.application.configuration.vat-percentage}")
  private String vatPercentage;
  @Value("${spring.application.configuration.tax-enterprise-percentage}")
  private String taxEnterprisePercentage;
  @Value("${spring.application.configuration.tax-customer-percentage}")
  private String taxCustomerPercentage;
  private static final String REPORT_NOT_FOUNT = "Report Not Found";
  private final ReportRepository reportRepository;
  private final ContractRepository contractRepository;
  private final ReportMapper reportMapper;

  /**
   * Get report information by id.
   *
   * @param id of report
   * @return report response
   */
  public ReportResponse getReport(final UUID id) {
    final UUID connectedUser = UserUtils.getConnectedUser();
    final ReportEntity reportEntity = reportRepository.findByIdAndCustomerId(id, connectedUser.toString())
        .orElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, REPORT_NOT_FOUNT));
    return reportMapper.toReportResponse(reportEntity);
  }

  /**
   * Get information for all report.
   *
   * @return report response
   */
  public List<ReportResponse> getAllReports() {
    return reportRepository.findAllByReportDataAndId(UserUtils.getConnectedUser().toString())
        .map(client -> client.stream()
            .map(reportMapper::toReportResponse)
            .toList())
        .orElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, REPORT_NOT_FOUNT));
  }

  /**
   * Create new report.
   *
   * @param reportRequest information
   * @return report response
   */
  public ReportResponse createReport(final ReportRequest reportRequest) {
    final UUID connectedUser = UserUtils.getConnectedUser();
    final ContractEntity contract = contractRepository
        .findByIdAndCustomerId(reportRequest.getContractId(), connectedUser.toString())
        .orElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, REPORT_NOT_FOUNT));

    final Calculated calculated = CalculationUtils.reportCalculate(contract, reportRequest, configMap());

    final ReportEntity report = reportMapper.toCreateReportEntity(reportRequest, calculated, connectedUser);
    final ReportEntity reportEntity = reportRepository.save(report);

    return reportMapper.toReportResponse(reportEntity);
  }

  /**
   * Update existing report.
   *
   * @param id of report
   * @param reportRequest information
   * @return report response
   */
  public ReportResponse updateReport(final UUID id, final ReportRequest reportRequest) {
    final UUID connectedUser = UserUtils.getConnectedUser();
    final Optional<ReportEntity> report = reportRepository.findByIdAndCustomerId(id, connectedUser.toString());

    if (report.isPresent()) {
      final ContractEntity contract = contractRepository
          .findByIdAndCustomerId(reportRequest.getContractId(), connectedUser.toString())
          .orElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, REPORT_NOT_FOUNT));
      final Calculated calculated = CalculationUtils.reportCalculate(contract, reportRequest, configMap());

      final ReportEntity clientEntity = Try.of(() -> reportRepository
              .save(reportMapper.toUpdateReportEntity(id, reportRequest, calculated, connectedUser)))
          .getOrElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, REPORT_NOT_FOUNT));
      return reportMapper.toReportResponse(clientEntity);
    } else {
      throw BusinessError.forError(ErrorHandler.NOT_FOUND, REPORT_NOT_FOUNT);
    }
  }

  /**
   * Delete report.
   *
   * @param id of report
   */
  public void deleteReport(final UUID id) {
    final Optional<ReportEntity> reportEntity = reportRepository.findByIdAndCustomerId(id,
        UserUtils.getConnectedUser().toString());
    if (reportEntity.isPresent()) {
      reportRepository.deleteById(id);
    } else {
      throw BusinessError.forError(ErrorHandler.NOT_FOUND, REPORT_NOT_FOUNT);
    }
  }

  private Map<String, String> configMap() {
    return Map.of(VAT_KEY, vatPercentage,
        TAX_CUSTOMER_KEY, taxCustomerPercentage,
        TAX_ENTERPRISE_KEY, taxEnterprisePercentage);
  }
}
