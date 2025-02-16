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
import org.grisbi.onefreelance.api.controller.ClientController;
import org.grisbi.onefreelance.api.controller.ControllerExceptionTranslator;
import org.grisbi.onefreelance.business.service.ClientService;
import org.grisbi.onefreelance.model.config.JacksonConfig;
import org.grisbi.onefreelance.model.dto.request.ClientRequest;
import org.grisbi.onefreelance.model.dto.response.ClientResponse;
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
@WebMvcTest(controllers = {ClientController.class})
@ContextConfiguration(classes = { ClientController.class, ControllerExceptionTranslator.class, JacksonConfig.class })
class ClientControllerTest {

  @MockitoBean
  ClientService clientService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void given_client_id_when_call_getClient_then_return_200_and_ClientResponse() throws Exception {
    final var client = Instancio.create(ClientResponse.class);
    given(clientService.getClient(any())).willReturn(client);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/client/{id}", client.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(client.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(client.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.siret").value(client.getSiret()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.referent").value(client.getReferent()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.address.number").value(client.getAddress().getNumber()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty());

    verify(clientService).getClient(any());
  }

  @Test
  void given_client_id_not_found_when_call_getClient_then_return_404() throws Exception {
    final var client = Instancio.create(ClientResponse.class);
    given(clientService.getClient(any())).willThrow(BusinessError.forError(ErrorHandler.NOT_FOUND));

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/client/{id}", client.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.error")
            .value(ErrorHandler.NOT_FOUND.getStatus().name()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status")
            .value(ErrorHandler.NOT_FOUND.getStatus().value()));
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_client_id_with_wrong_role_when_call_getClient_then_return_403() throws Exception {
    final var client = Instancio.create(ClientResponse.class);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/client/{id}", client.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void when_call_getAllClients_then_return_200_and_list_of_ClientResponse() throws Exception {
    final var client = Instancio.create(ClientResponse.class);
    given(clientService.getAllClients()).willReturn(List.of(client));

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/client")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(client.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(client.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].siret").value(client.getSiret()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].referent").value(client.getReferent()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].address.number").value(client.getAddress().getNumber()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].update_at").isNotEmpty());

    verify(clientService).getAllClients();
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void when_call_getAllClients_with_wrong_role_then_return_403() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/client")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_client_request_when_call_postClient_then_return_201_and_ClientResponse() throws Exception {
    final var clientRequest = Instancio.create(ClientRequest.class);
    final var client = Instancio.create(ClientResponse.class);
    given(clientService.createClient(any())).willReturn(client);

    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/client")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString(clientRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(client.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(client.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.siret").value(client.getSiret()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.referent").value(client.getReferent()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.address.number").value(client.getAddress().getNumber()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty());

    verify(clientService).createClient(clientRequest);
  }

  @Test
  void given_client_bad_request_when_call_postClient_then_return_400() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/client")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString("{\"field\": \"value\"}"))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_client_with_wrong_role_when_call_postClient_then_return_403() throws Exception {
    final var clientRequest = Instancio.create(ClientRequest.class);

    mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/client")
            .content(objectMapper.writeValueAsString(clientRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_ClientRequest_when_call_patchClient_then_return_200_and_ClientResponse() throws Exception {
    final var clientRequest = Instancio.create(ClientRequest.class);
    final var client = Instancio.create(ClientResponse.class);
    given(clientService.updateClient(any(), any())).willReturn(client);

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/client/{id}", client.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString(clientRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(client.getId().toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(client.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.siret").value(client.getSiret()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.referent").value(client.getReferent()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.address.number").value(client.getAddress().getNumber()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.create_at").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.update_at").isNotEmpty());

    verify(clientService).updateClient(client.getId(), clientRequest);
  }

  @Test
  void given_client_not_found_when_call_patchClient_then_return_404() throws Exception {
    final var clientRequest = Instancio.create(ClientRequest.class);
    final var client = Instancio.create(ClientResponse.class);
    given(clientService.updateClient(any(), any())).willThrow(BusinessError.forError(ErrorHandler.NOT_FOUND));

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/client/{id}", client.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString(clientRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void given_client_bad_request_when_call_patchClient_then_return_400() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/client/{id}", UUID.randomUUID())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .content(objectMapper.writeValueAsString("{\"field\": \"value\"}"))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_client_with_wrong_role_when_call_patchClient_then_return_403() throws Exception {
    final var clientRequest = Instancio.create(ClientRequest.class);

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/v1/client/{id}", UUID.randomUUID())
            .content(objectMapper.writeValueAsString(clientRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void given_client_id_when_call_deleteClient_then_return_204() throws Exception {
    final var client = Instancio.create(ClientResponse.class);
    doNothing().when(clientService).deleteClient(any());

    mockMvc.perform(MockMvcRequestBuilders
            .delete("/v1/client/{id}", client.getId())
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "ANONYMOUS")
  void given_client_id_with_wrong_role_when_call_deleteClient_then_return_204() throws Exception {
    final var client = Instancio.create(ClientResponse.class);

    mockMvc.perform(MockMvcRequestBuilders
            .delete("/v1/client/{id}", client.getId()))
        .andExpect(status().isForbidden());
  }

}
