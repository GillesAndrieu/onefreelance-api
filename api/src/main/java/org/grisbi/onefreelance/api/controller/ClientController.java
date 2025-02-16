package org.grisbi.onefreelance.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.grisbi.onefreelance.api.swagger.client.DeleteClientDocumentation;
import org.grisbi.onefreelance.api.swagger.client.GetAllClientsDocumentation;
import org.grisbi.onefreelance.api.swagger.client.GetClientDocumentation;
import org.grisbi.onefreelance.api.swagger.client.PatchClientDocumentation;
import org.grisbi.onefreelance.api.swagger.client.PostClientDocumentation;
import org.grisbi.onefreelance.business.service.ClientService;
import org.grisbi.onefreelance.model.dto.request.ClientRequest;
import org.grisbi.onefreelance.model.dto.response.ClientResponse;
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
 * Client rest controller.
 */
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/v1/client")
@RequiredArgsConstructor
@Validated
@Tag(name = "Client", description = "Use these API with JWT for all client action")
public class ClientController {

  private final ClientService clientService;

  /**
   * Get information of all client.
   *
   * @return all client
   */
  @GetAllClientsDocumentation
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ClientResponse>> getAllClients() {
    return ResponseEntity.ok(clientService.getAllClients());
  }

  /**
   * Get client information.
   *
   * @param id of client
   * @return client information
   */
  @GetClientDocumentation
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ClientResponse> getClient(@PathVariable final UUID id) {
    return ResponseEntity.ok(clientService.getClient(id));
  }

  /**
   * Create new client.
   *
   * @param clientRequest information
   * @return new client information
   */
  @PostClientDocumentation
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ClientResponse> postClient(@Valid @RequestBody final ClientRequest clientRequest) {
    return ResponseEntity.created(URI.create("/v1/client"))
        .body(clientService.createClient(clientRequest));
  }

  /**
   * Update client.
   *
   * @param id of client
   * @param clientRequest information
   * @return client updated
   */
  @PatchClientDocumentation
  @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ClientResponse> patchClient(
      @PathVariable final UUID id,
      @Valid @RequestBody final ClientRequest clientRequest) {
    return ResponseEntity.ok(clientService.updateClient(id, clientRequest));
  }

  /**
   * Delete client.
   *
   * @param id of client
   * @return 204 no content
   */
  @DeleteClientDocumentation
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteClient(
      @PathVariable final UUID id) {
    clientService.deleteClient(id);
    return ResponseEntity
        .status(HttpStatusCode.valueOf(HttpStatus.NO_CONTENT.value()))
        .build();
  }
}
