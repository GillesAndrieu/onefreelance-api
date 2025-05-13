package org.grisbi.onefreelance.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.grisbi.onefreelance.business.mappers.ReportMapper;
import org.grisbi.onefreelance.model.dto.request.ReportRequest;
import org.grisbi.onefreelance.model.dto.response.ReportResponse;
import org.grisbi.onefreelance.model.errors.BusinessError;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;
import org.grisbi.onefreelance.persistence.entity.ReportEntity;
import org.grisbi.onefreelance.persistence.repository.ContractRepository;
import org.grisbi.onefreelance.persistence.repository.ReportRepository;
import org.grisbi.onefreelance.security.dto.JwtUserDetails;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  @Mock
  ReportMapper reportmapper;
  @InjectMocks
  private ReportService reportService;
  @Mock
  private ReportRepository reportRepository;
  @Mock
  private ContractRepository contractRepository;

  @Test
  void given_report_id_when_call_getReport_then_return_reportResponse() {
    final var reportEntity = Instancio.create(ReportEntity.class);
    final var reportResponse = Instancio.of(ReportResponse.class)
        .set(field(ReportResponse::getId), reportEntity.getId())
        .create();
    createSecurityContext(reportEntity.getId());

    given(reportmapper.toReportResponse(reportEntity)).willReturn(reportResponse);
    given(reportRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.of(reportEntity));

    final var report = reportService.getReport(UUID.randomUUID());

    assertThat(report.getId()).isEqualTo(reportEntity.getId());
  }

  @Test
  void given_report_id_not_found_when_call_getReport_then_return_BusinessError() {
    final var id = UUID.randomUUID();

    createSecurityContext(id);
    given(reportRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.empty());

    assertThrows(BusinessError.class, () -> reportService.getReport(id));
  }

  @Test
  void when_call_getAllReports_then_return_reportResponse() {
    final var reportEntity = Instancio.create(ReportEntity.class);
    final var reportResponse = Instancio.of(ReportResponse.class)
        .set(field(ReportResponse::getId), reportEntity.getId())
        .create();
    createSecurityContext(reportEntity.getId());
    given(reportmapper.toReportResponse(reportEntity)).willReturn(reportResponse);
    given(reportRepository.findAllByReportDataAndId(any())).willReturn(Optional.of(List.of(reportEntity)));

    final var report = reportService.getAllReports();

    assertThat(report).isNotEmpty();
    assertThat(report.getFirst().getId()).isEqualTo(reportEntity.getId());
  }

  @Test
  void given_ReportRequest_when_call_createReport_then_return_reportResponse() {
    final var reportRequest = Instancio.create(ReportRequest.class);
    final var reportEntity = Instancio.create(ReportEntity.class);
    final var contractEntity = Instancio.create(ContractEntity.class);
    final var reportResponse = Instancio.of(ReportResponse.class)
        .set(field(ReportResponse::getId), reportEntity.getId())
        .create();
    createSecurityContext(reportEntity.getId());

    ReflectionTestUtils.setField(reportService, "vatPercentage", "0.2");
    ReflectionTestUtils.setField(reportService, "taxEnterprisePercentage", "0.234");
    ReflectionTestUtils.setField(reportService, "taxCustomerPercentage", "0.14");
    given(contractRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.of(contractEntity));
    given(reportmapper.toReportResponse(reportEntity)).willReturn(reportResponse);
    given(reportRepository.save(any())).willReturn(reportEntity);

    final var report = reportService.createReport(reportRequest);

    assertThat(report.getId()).isEqualTo(reportEntity.getId());
  }

  @Test
  void given_ReportRequest_when_call_updateReport_then_return_reportResponse() {
    final var reportRequest = Instancio.create(ReportRequest.class);
    final var reportEntity = Instancio.create(ReportEntity.class);
    final var contractEntity = Instancio.create(ContractEntity.class);
    final var reportResponse = Instancio.of(ReportResponse.class)
        .set(field(ReportResponse::getId), reportEntity.getId())
        .create();
    createSecurityContext(reportEntity.getId());

    ReflectionTestUtils.setField(reportService, "vatPercentage", "0.2");
    ReflectionTestUtils.setField(reportService, "taxEnterprisePercentage", "0.234");
    ReflectionTestUtils.setField(reportService, "taxCustomerPercentage", "0.14");
    given(contractRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.of(contractEntity));
    given(reportmapper.toReportResponse(reportEntity)).willReturn(reportResponse);
    given(reportRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.of(reportEntity));
    given(reportRepository.save(any())).willReturn(reportEntity);

    final var report = reportService.updateReport(UUID.randomUUID(), reportRequest);

    assertThat(report.getId()).isEqualTo(reportEntity.getId());
  }

  @Test
  void given_ReportRequest_and_report_not_found_when_call_updateReport_then_return_BusinessError() {
    final var id = UUID.randomUUID();
    final var reportRequest = Instancio.create(ReportRequest.class);
    createSecurityContext(UUID.randomUUID());

    given(reportRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.empty());

    assertThrows(BusinessError.class,
        () -> reportService.updateReport(id, reportRequest));
  }

  @Test
  void given_report_id_when_call_deleteReport_then_ok() {
    final var reportEntity = Instancio.create(ReportEntity.class);
    createSecurityContext(reportEntity.getId());

    given(reportRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.of(reportEntity));
    doNothing().when(reportRepository).deleteById(any());

    reportService.deleteReport(UUID.randomUUID());
    verify(reportRepository).deleteById(any());
  }

  @Test
  void given_fake_report_id_when_call_deleteReport_then_return_error() {
    final UUID reportId = UUID.randomUUID();
    createSecurityContext(reportId);

    given(reportRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.empty());

    assertThrows(BusinessError.class,
        () -> reportService.deleteReport(reportId));

    verify(reportRepository, times(0)).deleteById(any());
  }

  private void createSecurityContext(UUID id) {
    SecurityContextHolder.createEmptyContext();
    SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("user",
        new JwtUserDetails(id, "username", List.of(new SimpleGrantedAuthority("ROLE_USER"))),
        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
  }

}
