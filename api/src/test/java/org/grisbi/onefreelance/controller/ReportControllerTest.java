package org.grisbi.onefreelance.controller;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import org.grisbi.onefreelance.api.controller.ControllerExceptionTranslator;
import org.grisbi.onefreelance.api.controller.ReportController;
import org.grisbi.onefreelance.business.service.ReportService;
import org.grisbi.onefreelance.model.config.JacksonConfig;
import org.grisbi.onefreelance.model.dto.request.ReportRequest;
import org.grisbi.onefreelance.model.dto.response.ContractResponse;
import org.grisbi.onefreelance.model.dto.response.ReportResponse;
import org.grisbi.onefreelance.model.errors.BusinessError;
import org.grisbi.onefreelance.model.errors.ErrorHandler;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@EnableMethodSecurity
@WithMockUser(roles = "USER")
@WebMvcTest(controllers = {ReportController.class})
@ContextConfiguration(classes = { ReportController.class, ControllerExceptionTranslator.class, JacksonConfig.class })
class ReportControllerTest {

  @MockitoBean
  ReportService reportService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void given_report_id_when_call_getReport_then_return_200_and_ReportResponse() throws Exception {
    final var report = Instancio.of(ReportResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    given(reportService.getReport(any())).willReturn(report);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/report/{id}", report.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(report.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty());

    verify(reportService).getReport(any());
  }

  @Test
  void given_report_id_not_found_when_call_getReport_then_return_404() throws Exception {
    final var report = Instancio.create(ReportResponse.class);
    given(reportService.getReport(any())).willThrow(BusinessError.forError(ErrorHandler.NOT_FOUND));

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/report/{id}", report.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.error")
            .value(ErrorHandler.NOT_FOUND.getStatus().name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status")
            .value(ErrorHandler.NOT_FOUND.getStatus().value()));
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_report_id_with_wrong_role_when_call_getReport_then_return_403() throws Exception {
    final var report = Instancio.create(ReportResponse.class);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/report/{id}", report.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void when_call_getAllReports_then_return_200_and_list_of_ReportResponse() throws Exception {
    final var report = Instancio.of(ReportResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    given(reportService.getAllReports()).willReturn(List.of(report));

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/report")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(report.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].update_at").isNotEmpty());

    verify(reportService).getAllReports();
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void when_call_getAllReports_with_wrong_role_then_return_403() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/report")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_report_request_when_call_postReport_then_return_201_and_ReportResponse() throws Exception {
    final var reportRequest = Instancio.of(ReportRequest.class)
        .set(field(ReportRequest::getActivity), Instancio.ofMap(String.class, BigDecimal.class).size(30).create())
        .create();
    final var report = Instancio.of(ReportResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR"))
        .create();
    given(reportService.createReport(any())).willReturn(report);

    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/report")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString(reportRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(report.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty());

    verify(reportService).createReport(reportRequest);
  }

  @Test
  void given_report_bad_request_when_call_postReport_then_return_400() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/report")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString("{\"field\": \"value\"}"))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_report_with_wrong_role_when_call_postReport_then_return_403() throws Exception {
    final var reportRequest = Instancio.create(ReportRequest.class);

    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/report")
            .content(objectMapper.writeValueAsString(reportRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_ReportRequest_when_call_patchReport_then_return_200_and_ReportResponse() throws Exception {
    final var reportRequest = Instancio.of(ReportRequest.class)
        .set(field(ReportRequest::getActivity), Instancio.ofMap(String.class, BigDecimal.class).size(30).create())
        .create();
    final var report = Instancio.of(ReportResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR"))
        .create();
    given(reportService.updateReport(any(), any())).willReturn(report);

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/report/{id}", report.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString(reportRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(report.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty());

    verify(reportService).updateReport(report.getId(), reportRequest);
  }

  @Test
  void given_report_not_found_when_call_patchReport_then_return_404() throws Exception {
    final var reportRequest = Instancio.of(ReportRequest.class)
        .set(field(ReportRequest::getActivity), Instancio.ofMap(String.class, BigDecimal.class).size(30).create())
        .create();
    final var report = Instancio.create(ReportResponse.class);
    given(reportService.updateReport(any(), any())).willThrow(BusinessError.forError(ErrorHandler.NOT_FOUND));

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/report/{id}", report.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString(reportRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void given_report_bad_request_when_call_patchReport_then_return_400() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/report/{id}", UUID.randomUUID())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString("{\"field\": \"value\"}"))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_report_with_wrong_role_when_call_patchReport_then_return_403() throws Exception {
    final var reportRequest = Instancio.create(ReportRequest.class);

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/report/{id}", UUID.randomUUID())
            .content(objectMapper.writeValueAsString(reportRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_report_id_when_call_deleteReport_then_return_204() throws Exception {
    final var report = Instancio.create(ReportResponse.class);
    doNothing().when(reportService).deleteReport(any());

    mockMvc.perform(MockMvcRequestBuilders
            .delete("/v1/report/{id}", report.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_report_id_with_wrong_role_when_call_deleteReport_then_return_204() throws Exception {
    final var report = Instancio.create(ReportResponse.class);

    mockMvc.perform(MockMvcRequestBuilders
            .delete("/v1/report/{id}", report.getId()))
        .andExpect(status().isForbidden());
  }

}
