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
import org.grisbi.onefreelance.business.mappers.ClientMapper;
import org.grisbi.onefreelance.model.dto.request.ClientRequest;
import org.grisbi.onefreelance.model.dto.response.ClientResponse;
import org.grisbi.onefreelance.model.errors.BusinessError;
import org.grisbi.onefreelance.persistence.entity.ClientEntity;
import org.grisbi.onefreelance.persistence.repository.ClientRepository;
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

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

  @Mock
  ClientMapper clientmapper;
  @InjectMocks
  private ClientService clientService;
  @Mock
  private ClientRepository clientRepository;

  @Test
  void given_client_id_when_call_getClient_then_return_clientResponse() {
    final var clientEntity = Instancio.create(ClientEntity.class);
    final var clientResponse = Instancio.of(ClientResponse.class)
        .set(field(ClientResponse::getId), clientEntity.getId())
        .set(field(ClientResponse::getName), clientEntity.getClientData().getName())
        .set(field(ClientResponse::getSiret), clientEntity.getClientData().getSiret())
        .set(field(ClientResponse::getReferent), clientEntity.getClientData().getReferent())
        .create();
    given(clientmapper.toClientResponse(clientEntity)).willReturn(clientResponse);
    given(clientRepository.findById(any())).willReturn(Optional.of(clientEntity));

    final var client = clientService.getClient(UUID.randomUUID());

    assertThat(client.getId()).isEqualTo(clientEntity.getId());
    assertThat(client.getName()).isEqualTo(clientEntity.getClientData().getName());
    assertThat(client.getSiret()).isEqualTo(clientEntity.getClientData().getSiret());
    assertThat(client.getReferent()).isEqualTo(clientEntity.getClientData().getReferent());
  }

  @Test
  void given_client_id_not_found_when_call_getClient_then_return_BusinessError() {
    final var id = UUID.randomUUID();
    given(clientRepository.findById(any())).willReturn(Optional.empty());

    assertThrows(BusinessError.class, () -> clientService.getClient(id));
  }

  @Test
  void when_call_getAllClients_then_return_clientResponse() {
    final var clientEntity = Instancio.create(ClientEntity.class);
    final var clientResponse = Instancio.of(ClientResponse.class)
        .set(field(ClientResponse::getId), clientEntity.getId())
        .set(field(ClientResponse::getName), clientEntity.getClientData().getName())
        .set(field(ClientResponse::getSiret), clientEntity.getClientData().getSiret())
        .set(field(ClientResponse::getReferent), clientEntity.getClientData().getReferent())
        .create();
    given(clientmapper.toClientResponse(clientEntity)).willReturn(clientResponse);
    given(clientRepository.findAllByClientDataAndId(any())).willReturn(Optional.of(List.of(clientEntity)));

    final var client = clientService.getAllClients();

    assertThat(client).isNotEmpty();
    assertThat(client.getFirst().getId()).isEqualTo(clientEntity.getId());
    assertThat(client.getFirst().getName()).isEqualTo(clientEntity.getClientData().getName());
    assertThat(client.getFirst().getSiret()).isEqualTo(clientEntity.getClientData().getSiret());
    assertThat(client.getFirst().getReferent()).isEqualTo(clientEntity.getClientData().getReferent());
  }

  @Test
  void given_ClientRequest_when_call_createClient_then_return_clientResponse() {
    final var clientEntity = Instancio.create(ClientEntity.class);
    final var clientResponse = Instancio.of(ClientResponse.class)
        .set(field(ClientResponse::getId), clientEntity.getId())
        .set(field(ClientResponse::getName), clientEntity.getClientData().getName())
        .set(field(ClientResponse::getSiret), clientEntity.getClientData().getSiret())
        .set(field(ClientResponse::getReferent), clientEntity.getClientData().getReferent())
        .create();
    createSecurityContext(clientEntity.getId());

    given(clientmapper.toClientResponse(clientEntity)).willReturn(clientResponse);
    given(clientRepository.save(any())).willReturn(clientEntity);

    final var client = clientService.createClient(ClientRequest.builder().build());

    assertThat(client.getId()).isEqualTo(clientEntity.getId());
    assertThat(client.getName()).isEqualTo(clientEntity.getClientData().getName());
    assertThat(client.getSiret()).isEqualTo(clientEntity.getClientData().getSiret());
    assertThat(client.getReferent()).isEqualTo(clientEntity.getClientData().getReferent());
  }

  @Test
  void given_ClientRequest_when_call_updateClient_then_return_clientResponse() {
    final var clientEntity = Instancio.create(ClientEntity.class);
    final var clientResponse = Instancio.of(ClientResponse.class)
        .set(field(ClientResponse::getId), clientEntity.getId())
        .set(field(ClientResponse::getName), clientEntity.getClientData().getName())
        .set(field(ClientResponse::getSiret), clientEntity.getClientData().getSiret())
        .set(field(ClientResponse::getReferent), clientEntity.getClientData().getReferent())
        .create();
    createSecurityContext(clientEntity.getId());

    given(clientmapper.toClientResponse(clientEntity)).willReturn(clientResponse);
    given(clientRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.of(clientEntity));
    given(clientRepository.save(any())).willReturn(clientEntity);

    final var client = clientService.updateClient(UUID.randomUUID(), ClientRequest.builder().build());

    assertThat(client.getId()).isEqualTo(clientEntity.getId());
    assertThat(client.getName()).isEqualTo(clientEntity.getClientData().getName());
    assertThat(client.getSiret()).isEqualTo(clientEntity.getClientData().getSiret());
    assertThat(client.getReferent()).isEqualTo(clientEntity.getClientData().getReferent());
  }

  @Test
  void given_ClientRequest_and_client_not_found_when_call_updateClient_then_return_BusinessError() {
    final var id = UUID.randomUUID();
    final var clientRequest = Instancio.create(ClientRequest.class);
    createSecurityContext(UUID.randomUUID());

    given(clientRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.empty());

    assertThrows(BusinessError.class,
        () -> clientService.updateClient(id, clientRequest));
  }

  @Test
  void given_client_id_when_call_deleteClient_then_ok() {
    final var clientEntity = Instancio.create(ClientEntity.class);
    createSecurityContext(clientEntity.getId());

    given(clientRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.of(clientEntity));
    doNothing().when(clientRepository).deleteById(any());

    clientService.deleteClient(UUID.randomUUID());
    verify(clientRepository).deleteById(any());
  }

  @Test
  void given_fak_client_id_when_call_deleteClient_then_return_error() {
    final UUID clientId = UUID.randomUUID();
    createSecurityContext(clientId);

    given(clientRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.empty());

    assertThrows(BusinessError.class,
        () -> clientService.deleteClient(clientId));

    verify(clientRepository, times(0)).deleteById(any());
  }

  private void createSecurityContext(UUID id) {
    SecurityContextHolder.createEmptyContext();
    SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("user",
        new JwtUserDetails(id, "username", List.of(new SimpleGrantedAuthority("ROLE_USER"))),
        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
  }

}
