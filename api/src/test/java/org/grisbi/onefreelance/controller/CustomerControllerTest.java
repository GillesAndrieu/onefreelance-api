package org.grisbi.onefreelance.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import org.grisbi.onefreelance.api.controller.ControllerExceptionTranslator;
import org.grisbi.onefreelance.api.controller.CustomerController;
import org.grisbi.onefreelance.business.service.CustomerService;
import org.grisbi.onefreelance.model.dto.request.CustomerRequest;
import org.grisbi.onefreelance.model.dto.response.CustomerResponse;
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
@WithMockUser(roles = "ADMIN")
@WebMvcTest(controllers = {CustomerController.class})
@ContextConfiguration(classes = { CustomerController.class, ControllerExceptionTranslator.class })
class CustomerControllerTest {

  @MockitoBean
  CustomerService customerService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void given_customer_id_when_call_getCustomer_then_return_200_and_CustomerResponse() throws Exception {
    final var customer = Instancio.create(CustomerResponse.class);
    given(customerService.getCustomer(any())).willReturn(customer);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/customer/{id}", customer.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customer.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value(customer.getFirstname()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(customer.getLastname()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customer.getEmail()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.roles").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(customer.getActive()));

    verify(customerService).getCustomer(any());
  }

  @Test
  void given_customer_id_not_found_when_call_getCustomer_then_return_404() throws Exception {
    final var customer = Instancio.create(CustomerResponse.class);
    given(customerService.getCustomer(any())).willThrow(BusinessError.forError(ErrorHandler.NOT_FOUND));

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/customer/{id}", customer.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.error")
            .value(ErrorHandler.NOT_FOUND.getStatus().name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status")
            .value(ErrorHandler.NOT_FOUND.getStatus().value()));
  }

  @Test
  @WithMockUser(roles = "USER")
  void given_customer_id_with_wrong_role_when_call_getCustomer_then_return_403() throws Exception {
    final var customer = Instancio.create(CustomerResponse.class);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/customer/{id}", customer.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void when_call_getAllCustomers_then_return_200_and_list_of_CustomerResponse() throws Exception {
    final var customer = Instancio.create(CustomerResponse.class);
    given(customerService.getAllCustomers()).willReturn(List.of(customer));

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/customer")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(customer.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstname").value(customer.getFirstname()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastname").value(customer.getLastname()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email").value(customer.getEmail()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].roles").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].update_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].active").value(customer.getActive()));

    verify(customerService).getAllCustomers();
  }

  @Test
  @WithMockUser(roles = "USER")
  void when_call_getAllCustomers_with_wrong_role_then_return_403() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/customer")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_customer_request_when_call_postCustomer_then_return_201_and_CustomerResponse() throws Exception {
    final var customerRequest = Instancio.create(CustomerRequest.class);
    final var customer = Instancio.create(CustomerResponse.class);
    given(customerService.createCustomer(any())).willReturn(customer);

    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/customer")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
            .content(objectMapper.writeValueAsString(customerRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customer.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value(customer.getFirstname()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(customer.getLastname()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customer.getEmail()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.roles").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(customer.getActive()));

    verify(customerService).createCustomer(customerRequest);
  }

  @Test
  void given_customer_bad_request_when_call_postCustomer_then_return_400() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/customer")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
            .content(objectMapper.writeValueAsString("{\"field\": \"value\"}"))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "USER")
  void given_customer_with_wrong_role_when_call_postCustomer_then_return_403() throws Exception {
    final var customerRequest = Instancio.create(CustomerRequest.class);

    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/customer")
            .content(objectMapper.writeValueAsString(customerRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_CustomerRequest_when_call_patchCustomer_then_return_200_and_CustomerResponse() throws Exception {
    final var customerRequest = Instancio.create(CustomerRequest.class);
    final var customer = Instancio.create(CustomerResponse.class);
    given(customerService.updateCustomer(any(), any())).willReturn(customer);

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/customer/{id}", customer.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
            .content(objectMapper.writeValueAsString(customerRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customer.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value(customer.getFirstname()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(customer.getLastname()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customer.getEmail()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.roles").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(customer.getActive()));

    verify(customerService).updateCustomer(customer.getId(), customerRequest);
  }

  @Test
  void given_customer_not_found_when_call_patchCustomer_then_return_404() throws Exception {
    final var customerRequest = Instancio.create(CustomerRequest.class);
    final var customer = Instancio.create(CustomerResponse.class);
    given(customerService.updateCustomer(any(), any())).willThrow(BusinessError.forError(ErrorHandler.NOT_FOUND));

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/customer/{id}", customer.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
            .content(objectMapper.writeValueAsString(customerRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void given_customer_bad_request_when_call_patchCustomer_then_return_400() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/customer/{id}", UUID.randomUUID())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
            .content(objectMapper.writeValueAsString("{\"field\": \"value\"}"))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "USER")
  void given_customer_with_wrong_role_when_call_patchCustomer_then_return_403() throws Exception {
    final var customerRequest = Instancio.create(CustomerRequest.class);

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/customer/{id}", UUID.randomUUID())
            .content(objectMapper.writeValueAsString(customerRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_customer_id_when_call_deleteCustomer_then_return_204() throws Exception {
    final var customer = Instancio.create(CustomerResponse.class);
    doNothing().when(customerService).deleteCustomer(any());

    mockMvc.perform(MockMvcRequestBuilders
            .delete("/v1/customer/{id}", customer.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "USER")
  void given_customer_id_with_wrong_role_when_call_deleteCustomer_then_return_204() throws Exception {
    final var customer = Instancio.create(CustomerResponse.class);

    mockMvc.perform(MockMvcRequestBuilders
            .delete("/v1/customer/{id}", customer.getId()))
        .andExpect(status().isForbidden());
  }

}
