package org.grisbi.onefreelance.business.service;

import io.vavr.control.Try;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grisbi.onefreelance.business.mappers.ClientMapper;
import org.grisbi.onefreelance.model.dto.request.ClientRequest;
import org.grisbi.onefreelance.model.dto.response.ClientResponse;
import org.grisbi.onefreelance.model.errors.BusinessError;
import org.grisbi.onefreelance.model.errors.ErrorHandler;
import org.grisbi.onefreelance.persistence.entity.ClientEntity;
import org.grisbi.onefreelance.persistence.repository.ClientRepository;
import org.springframework.stereotype.Service;

/**
 * Client Service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

  private final ClientRepository clientRepository;
  private final ClientMapper clientMapper;

  /**
   * Get client information by id.
   *
   * @param id of client
   * @return client response
   */
  public ClientResponse getClient(final UUID id) {
    final ClientEntity clientEntity = clientRepository.findById(id)
        .orElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, "Client Not Found"));
    return clientMapper.toClientResponse(clientEntity);
  }

  /**
   * Get information for all clients.
   *
   * @return client response
   */
  public List<ClientResponse> getAllClients() {
    return clientRepository.findAll()
        .stream()
        .map(clientMapper::toClientResponse)
        .toList();
  }

  /**
   * Create new client.
   *
   * @param clientRequest information
   * @return client response
   */
  public ClientResponse createClient(final ClientRequest clientRequest) {
    final ClientEntity clientEntity = clientRepository
        .save(clientMapper.toCreateClientEntity(clientRequest));
    return clientMapper.toClientResponse(clientEntity);
  }

  /**
   * Update existing client.
   *
   * @param id of client
   * @param clientRequest information
   * @return client response
   */
  public ClientResponse updateClient(final UUID id, final ClientRequest clientRequest) {
    final ClientEntity clientEntity = Try.of(() -> clientRepository
            .save(clientMapper.toUpdateClientEntity(id, clientRequest)))
        .getOrElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, "Client Not Found"));
    return clientMapper.toClientResponse(clientEntity);
  }

  /**
   * Delete client.
   *
   * @param id of client
   */
  public void deleteClient(final UUID id) {
    clientRepository.deleteById(id);
  }
}
