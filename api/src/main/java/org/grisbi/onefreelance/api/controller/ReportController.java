package org.grisbi.onefreelance.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.grisbi.onefreelance.api.swagger.report.DeleteReportDocumentation;
import org.grisbi.onefreelance.api.swagger.report.GetAllReportsByYearDocumentation;
import org.grisbi.onefreelance.api.swagger.report.GetAllReportsDocumentation;
import org.grisbi.onefreelance.api.swagger.report.GetReportDocumentation;
import org.grisbi.onefreelance.api.swagger.report.GetReportsYearsDocumentation;
import org.grisbi.onefreelance.api.swagger.report.PatchReportDocumentation;
import org.grisbi.onefreelance.api.swagger.report.PostReportDocumentation;
import org.grisbi.onefreelance.business.service.ReportService;
import org.grisbi.onefreelance.model.dto.request.ReportRequest;
import org.grisbi.onefreelance.model.dto.response.ReportResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Report rest controller.
 */
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/v1/report")
@RequiredArgsConstructor
@Validated
@Tag(name = "Report", description = "Use these API with JWT for all report action")
public class ReportController {

  private final ReportService reportService;

  /**
   * Get all distinct years for report created.
   *
   * @return all year
   */
  @GetReportsYearsDocumentation
  @GetMapping(value = "/years", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Integer>> getReportsDistinctYears() {
    return ResponseEntity.ok(reportService.getReportsDistinctYears());
  }

  /**
   * Get all reports by year.
   *
   * @return all reports
   */
  @GetAllReportsByYearDocumentation
  @GetMapping(value = "/year/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ReportResponse>> getReportsByYear(@PathVariable final String year) {
    return ResponseEntity.ok(reportService.getReportsBYear(year));
  }

  /**
   * Get information of all report.
   *
   * @return all report
   */
  @GetAllReportsDocumentation
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ReportResponse>> getAllReports() {
    return ResponseEntity.ok(reportService.getAllReports());
  }

  /**
   * Get report information.
   *
   * @param id of report
   * @return report information
   */
  @GetReportDocumentation
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReportResponse> getReport(@PathVariable final UUID id) {
    return ResponseEntity.ok(reportService.getReport(id));
  }

  /**
   * Create new report.
   *
   * @param reportRequest information
   * @return new report information
   */
  @PostReportDocumentation
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReportResponse> postReport(@Valid @RequestBody final ReportRequest reportRequest) {
    return ResponseEntity.created(URI.create("/v1/report"))
        .body(reportService.createReport(reportRequest));
  }

  /**
   * Update report.
   *
   * @param id of report
   * @param reportRequest information
   * @return report updated
   */
  @PatchReportDocumentation
  @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReportResponse> patchReport(
      @PathVariable final UUID id,
      @Valid @RequestBody final ReportRequest reportRequest) {
    return ResponseEntity.ok(reportService.updateReport(id, reportRequest));
  }

  /**
   * Delete report.
   *
   * @param id of report
   * @return 204 no content
   */
  @DeleteReportDocumentation
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReport(@PathVariable final UUID id) {
    reportService.deleteReport(id);
    return ResponseEntity
        .status(HttpStatusCode.valueOf(HttpStatus.NO_CONTENT.value()))
        .build();
  }
}
