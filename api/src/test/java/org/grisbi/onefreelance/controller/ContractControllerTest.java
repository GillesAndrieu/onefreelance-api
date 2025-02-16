package org.grisbi.onefreelance.controller;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import org.grisbi.onefreelance.api.controller.ContractController;
import org.grisbi.onefreelance.api.controller.ControllerExceptionTranslator;
import org.grisbi.onefreelance.business.service.ContractService;
import org.grisbi.onefreelance.model.config.JacksonConfig;
import org.grisbi.onefreelance.model.dto.request.ContractRequest;
import org.grisbi.onefreelance.model.dto.response.ContractResponse;
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
@WebMvcTest(controllers = {ContractController.class})
@ContextConfiguration(classes = { ContractController.class, ControllerExceptionTranslator.class, JacksonConfig.class })
class ContractControllerTest {

  @MockitoBean
  ContractService contractService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void given_contract_id_when_call_getContract_then_return_200_and_ContractResponse() throws Exception {
    final var contract = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    given(contractService.getContract(any())).willReturn(contract);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/contract/{id}", contract.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(contract.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contract.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(contract.getNumber()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.tax_rate").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.daily_rate").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.tax_rate_type").value(contract.getTaxRateType().name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.currency_daily_rate")
            .value(contract.getCurrencyDailyRate().getCurrencyCode()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty());

    verify(contractService).getContract(any());
  }

  @Test
  void given_contract_id_not_found_when_call_getContract_then_return_404() throws Exception {
    final var contract = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    given(contractService.getContract(any())).willThrow(BusinessError.forError(ErrorHandler.NOT_FOUND));

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/contract/{id}", contract.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.error")
            .value(ErrorHandler.NOT_FOUND.getStatus().name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status")
            .value(ErrorHandler.NOT_FOUND.getStatus().value()));
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_contract_id_with_wrong_role_when_call_getContract_then_return_403() throws Exception {
    final var contract = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR")).create();

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/contract/{id}", contract.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void when_call_getAllContracts_then_return_200_and_list_of_ContractResponse() throws Exception {
    final var contract = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    given(contractService.getAllContracts()).willReturn(List.of(contract));

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/contract")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(contract.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(contract.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].number").value(contract.getNumber()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].tax_rate").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].daily_rate").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].tax_rate_type")
            .value(contract.getTaxRateType().name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].currency_daily_rate")
            .value(contract.getCurrencyDailyRate().getCurrencyCode()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].update_at").isNotEmpty());

    verify(contractService).getAllContracts();
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void when_call_getAllContracts_with_wrong_role_then_return_403() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/contract")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_contract_request_when_call_postContract_then_return_201_and_ContractResponse() throws Exception {
    final var contractRequest = Instancio.of(ContractRequest.class)
        .set(field(ContractRequest::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    final var contract = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    given(contractService.createContract(any())).willReturn(contract);

    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/contract")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString(contractRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(contract.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contract.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(contract.getNumber()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.tax_rate").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.daily_rate").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.tax_rate_type").value(contract.getTaxRateType().name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.currency_daily_rate")
            .value(contract.getCurrencyDailyRate().getCurrencyCode()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty());

    verify(contractService).createContract(contractRequest);
  }

  @Test
  void given_contract_bad_request_when_call_postContract_then_return_400() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/contract")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString("{\"field\": \"value\"}"))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_contract_with_wrong_role_when_call_postContract_then_return_403() throws Exception {
    final var contractRequest = Instancio.of(ContractRequest.class)
        .set(field(ContractRequest::getCurrencyDailyRate), Currency.getInstance("EUR")).create();

    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/contract")
            .content(objectMapper.writeValueAsString(contractRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_ContractRequest_when_call_patchContract_then_return_200_and_ContractResponse() throws Exception {
    final var contractRequest = Instancio.of(ContractRequest.class)
        .set(field(ContractRequest::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    final var contract = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    given(contractService.updateContract(any(), any())).willReturn(contract);

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/contract/{id}", contract.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString(contractRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(contract.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contract.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(contract.getNumber()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.tax_rate").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.daily_rate").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.tax_rate_type").value(contract.getTaxRateType().name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.currency_daily_rate")
            .value(contract.getCurrencyDailyRate().getCurrencyCode()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty());

    verify(contractService).updateContract(contract.getId(), contractRequest);
  }

  @Test
  void given_contract_not_found_when_call_patchContract_then_return_404() throws Exception {
    final var contractRequest = Instancio.of(ContractRequest.class)
        .set(field(ContractRequest::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    final var contract = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    given(contractService.updateContract(any(), any())).willThrow(BusinessError.forError(ErrorHandler.NOT_FOUND));

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/contract/{id}", contract.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString(contractRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void given_contract_bad_request_when_call_patchContract_then_return_400() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/contract/{id}", UUID.randomUUID())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString("{\"field\": \"value\"}"))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_contract_with_wrong_role_when_call_patchContract_then_return_403() throws Exception {
    final var contractRequest = Instancio.of(ContractRequest.class)
        .set(field(ContractRequest::getCurrencyDailyRate), Currency.getInstance("EUR")).create();

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/contract/{id}", UUID.randomUUID())
            .content(objectMapper.writeValueAsString(contractRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_contract_id_when_call_deleteContract_then_return_204() throws Exception {
    final var contract = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR")).create();
    doNothing().when(contractService).deleteContract(any());

    mockMvc.perform(MockMvcRequestBuilders
            .delete("/v1/contract/{id}", contract.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_contract_id_with_wrong_role_when_call_deleteContract_then_return_204() throws Exception {
    final var contract = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getCurrencyDailyRate), Currency.getInstance("EUR")).create();

    mockMvc.perform(MockMvcRequestBuilders
            .delete("/v1/contract/{id}", contract.getId()))
        .andExpect(status().isForbidden());
  }

}
