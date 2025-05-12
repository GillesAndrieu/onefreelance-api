package org.grisbi.onefreelance.business.service;

import io.vavr.control.Try;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grisbi.onefreelance.business.mappers.ContractMapper;
import org.grisbi.onefreelance.business.utils.UserUtils;
import org.grisbi.onefreelance.model.dto.request.ContractRequest;
import org.grisbi.onefreelance.model.dto.response.ContractResponse;
import org.grisbi.onefreelance.model.errors.BusinessError;
import org.grisbi.onefreelance.model.errors.ErrorHandler;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;
import org.grisbi.onefreelance.persistence.repository.ContractRepository;
import org.springframework.stereotype.Service;

/**
 * Contract Service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

  private static final String CONTRACT_NOT_FOUNT = "Contract Not Found";
  private final ContractRepository contractRepository;
  private final ContractMapper contractMapper;

  /**
   * Get contract information by id.
   *
   * @param id of contract
   * @return contract response
   */
  public ContractResponse getContract(final UUID id) {
    final UUID connectedUser = UserUtils.getConnectedUser();
    final ContractEntity contractEntity = contractRepository.findByIdAndCustomerId(id, connectedUser.toString())
        .orElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, CONTRACT_NOT_FOUNT));
    return contractMapper.toContractResponse(contractEntity);
  }

  /**
   * Get information for all contracts.
   *
   * @return contract response
   */
  public List<ContractResponse> getAllContracts() {
    return contractRepository.findAllByContractDataAndId(UserUtils.getConnectedUser().toString())
        .map(contract -> contract.stream()
            .map(contractMapper::toContractResponse)
            .toList())
        .orElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, CONTRACT_NOT_FOUNT));
  }

  /**
   * Create new contract.
   *
   * @param contractRequest information
   * @return contract response
   */
  public ContractResponse createContract(final ContractRequest contractRequest) {
    final ContractEntity contractEntity = contractRepository
        .save(contractMapper.toCreateContractEntity(contractRequest, UserUtils.getConnectedUser()));
    return contractMapper.toContractResponse(contractEntity);
  }

  /**
   * Update existing contract.
   *
   * @param id of customer
   * @param contractRequest information
   * @return contract response
   */
  public ContractResponse updateContract(final UUID id, final ContractRequest contractRequest) {
    final UUID connectedUser = UserUtils.getConnectedUser();
    final Optional<ContractEntity> contract = contractRepository.findByIdAndCustomerId(id, connectedUser.toString());

    if (contract.isPresent()) {
      final ContractEntity contractEntity = Try.of(() -> contractRepository
              .save(contractMapper.toUpdateContractEntity(id, contractRequest, connectedUser)))
          .getOrElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, CONTRACT_NOT_FOUNT));
      return contractMapper.toContractResponse(contractEntity);
    } else {
      throw BusinessError.forError(ErrorHandler.NOT_FOUND, CONTRACT_NOT_FOUNT);
    }
  }

  /**
   * Delete contract.
   *
   * @param id of contract
   */
  public void deleteContract(final UUID id) {

    final Optional<ContractEntity> contractEntity = contractRepository.findByIdAndCustomerId(id,
        UserUtils.getConnectedUser().toString());
    if (contractEntity.isPresent()) {
      contractRepository.deleteById(id);
    } else {
      throw BusinessError.forError(ErrorHandler.NOT_FOUND, CONTRACT_NOT_FOUNT);
    }
  }
}
