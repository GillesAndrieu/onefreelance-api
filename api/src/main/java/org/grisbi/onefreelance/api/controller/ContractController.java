package org.grisbi.onefreelance.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.grisbi.onefreelance.api.swagger.contract.DeleteContractDocumentation;
import org.grisbi.onefreelance.api.swagger.contract.GetAllContractsDocumentation;
import org.grisbi.onefreelance.api.swagger.contract.GetContractDocumentation;
import org.grisbi.onefreelance.api.swagger.contract.PatchContractDocumentation;
import org.grisbi.onefreelance.api.swagger.contract.PostContractDocumentation;
import org.grisbi.onefreelance.business.service.ContractService;
import org.grisbi.onefreelance.model.dto.request.ContractRequest;
import org.grisbi.onefreelance.model.dto.response.ContractResponse;
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
 * Contract rest controller.
 */
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/v1/contract")
@RequiredArgsConstructor
@Validated
@Tag(name = "Contract", description = "Use these API with JWT for all contract action")
public class ContractController {

  private final ContractService contractService;

  /**
   * Get information of all contract.
   *
   * @return all contract
   */
  @GetAllContractsDocumentation
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ContractResponse>> getAllContracts() {
    return ResponseEntity.ok(contractService.getAllContracts());
  }

  /**
   * Get contract information.
   *
   * @param id of contract
   * @return contract information
   */
  @GetContractDocumentation
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ContractResponse> getContract(@PathVariable final UUID id) {
    return ResponseEntity.ok(contractService.getContract(id));
  }

  /**
   * Create new contract.
   *
   * @param contractRequest information
   * @return new contract information
   */
  @PostContractDocumentation
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ContractResponse> postContract(@Valid @RequestBody final ContractRequest contractRequest) {
    return ResponseEntity.created(URI.create("/v1/contract"))
        .body(contractService.createContract(contractRequest));
  }

  /**
   * Update contract.
   *
   * @param id of contract
   * @param contractRequest information
   * @return contract updated
   */
  @PatchContractDocumentation
  @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ContractResponse> patchContract(
      @PathVariable final UUID id,
      @Valid @RequestBody final ContractRequest contractRequest) {
    return ResponseEntity.ok(contractService.updateContract(id, contractRequest));
  }

  /**
   * Delete contract.
   *
   * @param id of contract
   * @return 204 no content
   */
  @DeleteContractDocumentation
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteContract(
      @PathVariable final UUID id) {
    contractService.deleteContract(id);
    return ResponseEntity
        .status(HttpStatusCode.valueOf(HttpStatus.NO_CONTENT.value()))
        .build();
  }
}
