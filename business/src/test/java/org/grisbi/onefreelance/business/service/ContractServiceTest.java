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
import org.grisbi.onefreelance.business.mappers.ContractMapper;
import org.grisbi.onefreelance.model.dto.request.ContractRequest;
import org.grisbi.onefreelance.model.dto.response.ContractResponse;
import org.grisbi.onefreelance.model.errors.BusinessError;
import org.grisbi.onefreelance.persistence.entity.ContractEntity;
import org.grisbi.onefreelance.persistence.repository.ContractRepository;
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
class ContractServiceTest {

  @Mock
  ContractMapper contractMapper;
  @InjectMocks
  private ContractService contractService;
  @Mock
  private ContractRepository contractRepository;

  @Test
  void given_contract_id_when_call_getContract_then_return_contractResponse() {
    final var contractEntity = Instancio.create(ContractEntity.class);
    final var contractResponse = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getId), contractEntity.getId())
        .set(field(ContractResponse::getName), contractEntity.getContractData().getName())
        .set(field(ContractResponse::getNumber), contractEntity.getContractData().getNumber())
        .set(field(ContractResponse::getDailyRate), contractEntity.getContractData().getDailyRate())
        .set(field(ContractResponse::getCurrencyDailyRate), contractEntity.getContractData().getCurrencyDailyRate())
        .set(field(ContractResponse::getTaxRate), contractEntity.getContractData().getTaxRate())
        .set(field(ContractResponse::getTaxRateType), contractEntity.getContractData().getTaxRateType())
        .create();
    createSecurityContext(contractEntity.getId());

    given(contractMapper.toContractJoinClientResponse(contractEntity)).willReturn(contractResponse);
    given(contractRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.of(contractEntity));

    final var contract = contractService.getContract(UUID.randomUUID());

    assertThat(contract.getId()).isEqualTo(contractEntity.getId());
    assertThat(contract.getName()).isEqualTo(contractEntity.getContractData().getName());
    assertThat(contract.getNumber()).isEqualTo(contractEntity.getContractData().getNumber());
    assertThat(contract.getDailyRate()).isEqualTo(contractEntity.getContractData().getDailyRate());
    assertThat(contract.getCurrencyDailyRate()).isEqualTo(contractEntity.getContractData().getCurrencyDailyRate());
    assertThat(contract.getTaxRate()).isEqualTo(contractEntity.getContractData().getTaxRate());
    assertThat(contract.getTaxRateType()).isEqualTo(contractEntity.getContractData().getTaxRateType());
  }

  @Test
  void given_contract_id_not_found_when_call_getContract_then_return_BusinessError() {
    final var id = UUID.randomUUID();
    createSecurityContext(id);

    given(contractRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.empty());

    assertThrows(BusinessError.class, () -> contractService.getContract(id));
  }

  @Test
  void when_call_getAllContracts_then_return_contractResponse() {
    final var contractEntity = Instancio.create(ContractEntity.class);
    final var contractResponse = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getId), contractEntity.getId())
        .set(field(ContractResponse::getName), contractEntity.getContractData().getName())
        .set(field(ContractResponse::getNumber), contractEntity.getContractData().getNumber())
        .set(field(ContractResponse::getDailyRate), contractEntity.getContractData().getDailyRate())
        .set(field(ContractResponse::getCurrencyDailyRate), contractEntity.getContractData().getCurrencyDailyRate())
        .set(field(ContractResponse::getTaxRate), contractEntity.getContractData().getTaxRate())
        .set(field(ContractResponse::getTaxRateType), contractEntity.getContractData().getTaxRateType())
        .create();

    given(contractMapper.toContractJoinClientResponse(contractEntity)).willReturn(contractResponse);
    given(contractRepository.findAllDataById(any())).willReturn(Optional.of(List.of(contractEntity)));

    final var contract = contractService.getAllContracts();

    assertThat(contract).isNotEmpty();
    assertThat(contract.getFirst().getId()).isEqualTo(contractEntity.getId());
    assertThat(contract.getFirst().getName()).isEqualTo(contractEntity.getContractData().getName());
    assertThat(contract.getFirst().getNumber()).isEqualTo(contractEntity.getContractData().getNumber());
    assertThat(contract.getFirst().getDailyRate()).isEqualTo(contractEntity.getContractData().getDailyRate());
    assertThat(contract.getFirst().getCurrencyDailyRate()).isEqualTo(contractEntity.getContractData().getCurrencyDailyRate());
    assertThat(contract.getFirst().getTaxRate()).isEqualTo(contractEntity.getContractData().getTaxRate());
    assertThat(contract.getFirst().getTaxRateType()).isEqualTo(contractEntity.getContractData().getTaxRateType());
  }

  @Test
  void given_ContractRequest_when_call_createContract_then_return_contractResponse() {
    final var contractEntity = Instancio.create(ContractEntity.class);
    final var contractResponse = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getId), contractEntity.getId())
        .set(field(ContractResponse::getName), contractEntity.getContractData().getName())
        .set(field(ContractResponse::getNumber), contractEntity.getContractData().getNumber())
        .set(field(ContractResponse::getDailyRate), contractEntity.getContractData().getDailyRate())
        .set(field(ContractResponse::getCurrencyDailyRate), contractEntity.getContractData().getCurrencyDailyRate())
        .set(field(ContractResponse::getTaxRate), contractEntity.getContractData().getTaxRate())
        .set(field(ContractResponse::getTaxRateType), contractEntity.getContractData().getTaxRateType())
        .create();
    given(contractMapper.toContractResponse(contractEntity)).willReturn(contractResponse);
    given(contractRepository.save(any())).willReturn(contractEntity);

    final var contract = contractService.createContract(ContractRequest.builder().build());

    assertThat(contract.getId()).isEqualTo(contractEntity.getId());
    assertThat(contract.getName()).isEqualTo(contractEntity.getContractData().getName());
    assertThat(contract.getNumber()).isEqualTo(contractEntity.getContractData().getNumber());
    assertThat(contract.getDailyRate()).isEqualTo(contractEntity.getContractData().getDailyRate());
    assertThat(contract.getCurrencyDailyRate()).isEqualTo(contractEntity.getContractData().getCurrencyDailyRate());
    assertThat(contract.getTaxRate()).isEqualTo(contractEntity.getContractData().getTaxRate());
    assertThat(contract.getTaxRateType()).isEqualTo(contractEntity.getContractData().getTaxRateType());
  }

  @Test
  void given_ContractRequest_when_call_updateContract_then_return_contractResponse() {
    final var contractEntity = Instancio.create(ContractEntity.class);
    final var contractResponse = Instancio.of(ContractResponse.class)
        .set(field(ContractResponse::getId), contractEntity.getId())
        .set(field(ContractResponse::getName), contractEntity.getContractData().getName())
        .set(field(ContractResponse::getNumber), contractEntity.getContractData().getNumber())
        .set(field(ContractResponse::getDailyRate), contractEntity.getContractData().getDailyRate())
        .set(field(ContractResponse::getCurrencyDailyRate), contractEntity.getContractData().getCurrencyDailyRate())
        .set(field(ContractResponse::getTaxRate), contractEntity.getContractData().getTaxRate())
        .set(field(ContractResponse::getTaxRateType), contractEntity.getContractData().getTaxRateType())
        .create();
    createSecurityContext(contractEntity.getId());

    given(contractMapper.toContractResponse(contractEntity)).willReturn(contractResponse);
    given(contractRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.of(contractEntity));
    given(contractRepository.save(any())).willReturn(contractEntity);

    final var contract = contractService.updateContract(UUID.randomUUID(), ContractRequest.builder().build());

    assertThat(contract.getId()).isEqualTo(contractEntity.getId());
    assertThat(contract.getName()).isEqualTo(contractEntity.getContractData().getName());
    assertThat(contract.getNumber()).isEqualTo(contractEntity.getContractData().getNumber());
    assertThat(contract.getDailyRate()).isEqualTo(contractEntity.getContractData().getDailyRate());
    assertThat(contract.getCurrencyDailyRate()).isEqualTo(contractEntity.getContractData().getCurrencyDailyRate());
    assertThat(contract.getTaxRate()).isEqualTo(contractEntity.getContractData().getTaxRate());
    assertThat(contract.getTaxRateType()).isEqualTo(contractEntity.getContractData().getTaxRateType());
  }

  @Test
  void given_ContractRequest_and_contract_not_found_when_call_updateContract_then_return_BusinessError() {
    final var id = UUID.randomUUID();
    final var contractRequest = Instancio.create(ContractRequest.class);
    createSecurityContext(UUID.randomUUID());

    given(contractRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.empty());

    assertThrows(BusinessError.class,
        () -> contractService.updateContract(id, contractRequest));
  }

  @Test
  void given_contract_id_when_call_deleteContract_then_ok() {
    final var contractEntity = Instancio.create(ContractEntity.class);
    createSecurityContext(UUID.randomUUID());

    given(contractRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.of(contractEntity));
    doNothing().when(contractRepository).deleteContract(any());

    contractService.deleteContract(UUID.randomUUID());
    verify(contractRepository).deleteContract(any());
  }

  @Test
  void given_fake_contract_id_when_call_deleteContract_then_return_error() {
    final UUID clientId = UUID.randomUUID();
    createSecurityContext(clientId);

    given(contractRepository.findByIdAndCustomerId(any(), any())).willReturn(Optional.empty());

    assertThrows(BusinessError.class,
        () -> contractService.deleteContract(clientId));

    verify(contractRepository, times(0)).deleteById(any());
  }

  private void createSecurityContext(UUID id) {
    SecurityContextHolder.createEmptyContext();
    SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("user",
        new JwtUserDetails(id, "username", List.of(new SimpleGrantedAuthority("ROLE_USER"))),
        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
  }

}
