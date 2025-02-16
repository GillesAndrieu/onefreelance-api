package org.grisbi.onefreelance.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.grisbi.onefreelance.business.mappers.CustomerMapper;
import org.grisbi.onefreelance.model.dto.request.CustomerRequest;
import org.grisbi.onefreelance.model.dto.response.CustomerResponse;
import org.grisbi.onefreelance.model.errors.BusinessError;
import org.grisbi.onefreelance.persistence.entity.CustomerEntity;
import org.grisbi.onefreelance.persistence.repository.CustomerRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  @Mock
  CustomerMapper customerMapper;
  @InjectMocks
  private CustomerService customerService;
  @Mock
  private CustomerRepository customerRepository;

  @Test
  void given_customer_id_when_call_getCustomer_then_return_customerResponse() {
    final var customerEntity = Instancio.create(CustomerEntity.class);
    final var customerResponse = Instancio.of(CustomerResponse.class)
        .set(field(CustomerResponse::getId), customerEntity.getId())
        .set(field(CustomerResponse::getFirstname), customerEntity.getCustomerData().getFirstname())
        .set(field(CustomerResponse::getLastname), customerEntity.getCustomerData().getLastname())
        .set(field(CustomerResponse::getActive), customerEntity.getCustomerData().getActive())
        .set(field(CustomerResponse::getEmail), customerEntity.getCustomerData().getEmail())
        .set(field(CustomerResponse::getRoles), customerEntity.getCustomerData().getRoles())
        .create();
    given(customerMapper.toCustomerResponse(customerEntity)).willReturn(customerResponse);
    given(customerRepository.findById(any())).willReturn(Optional.of(customerEntity));

    final var customer = customerService.getCustomer(UUID.randomUUID());

    assertThat(customer.getId()).isEqualTo(customerEntity.getId());
    assertThat(customer.getActive()).isEqualTo(customerEntity.getCustomerData().getActive());
    assertThat(customer.getLastname()).isEqualTo(customerEntity.getCustomerData().getLastname());
    assertThat(customer.getFirstname()).isEqualTo(customerEntity.getCustomerData().getFirstname());
    assertThat(customer.getEmail()).isEqualTo(customerEntity.getCustomerData().getEmail());
    assertThat(customer.getRoles()).isEqualTo(customerEntity.getCustomerData().getRoles());
  }

  @Test
  void given_customer_id_not_found_when_call_getCustomer_then_return_BusinessError() {
    final var id = UUID.randomUUID();
    given(customerRepository.findById(any())).willReturn(Optional.empty());

    assertThrows(BusinessError.class, () -> customerService.getCustomer(id));
  }

  @Test
  void when_call_getAllCustomer_then_return_customerResponse() {
    final var customerEntity = Instancio.create(CustomerEntity.class);
    final var customerResponse = Instancio.of(CustomerResponse.class)
        .set(field(CustomerResponse::getId), customerEntity.getId())
        .set(field(CustomerResponse::getFirstname), customerEntity.getCustomerData().getFirstname())
        .set(field(CustomerResponse::getLastname), customerEntity.getCustomerData().getLastname())
        .set(field(CustomerResponse::getActive), customerEntity.getCustomerData().getActive())
        .set(field(CustomerResponse::getEmail), customerEntity.getCustomerData().getEmail())
        .set(field(CustomerResponse::getRoles), customerEntity.getCustomerData().getRoles())
        .create();
    given(customerMapper.toCustomerResponse(customerEntity)).willReturn(customerResponse);
    given(customerRepository.findAll()).willReturn(List.of(customerEntity));

    final var customer = customerService.getAllCustomers();

    assertThat(customer).isNotEmpty();
    assertThat(customer.getFirst().getId()).isEqualTo(customerEntity.getId());
    assertThat(customer.getFirst().getActive()).isEqualTo(customerEntity.getCustomerData().getActive());
    assertThat(customer.getFirst().getLastname()).isEqualTo(customerEntity.getCustomerData().getLastname());
    assertThat(customer.getFirst().getFirstname()).isEqualTo(customerEntity.getCustomerData().getFirstname());
    assertThat(customer.getFirst().getEmail()).isEqualTo(customerEntity.getCustomerData().getEmail());
    assertThat(customer.getFirst().getRoles()).isEqualTo(customerEntity.getCustomerData().getRoles());
  }

  @Test
  void given_CustomerRequest_when_call_createCustomer_then_return_customerResponse() {
    final var customerEntity = Instancio.create(CustomerEntity.class);
    final var customerResponse = Instancio.of(CustomerResponse.class)
        .set(field(CustomerResponse::getId), customerEntity.getId())
        .set(field(CustomerResponse::getFirstname), customerEntity.getCustomerData().getFirstname())
        .set(field(CustomerResponse::getLastname), customerEntity.getCustomerData().getLastname())
        .set(field(CustomerResponse::getActive), customerEntity.getCustomerData().getActive())
        .set(field(CustomerResponse::getEmail), customerEntity.getCustomerData().getEmail())
        .set(field(CustomerResponse::getRoles), customerEntity.getCustomerData().getRoles())
        .create();
    given(customerMapper.toCustomerResponse(customerEntity)).willReturn(customerResponse);
    given(customerRepository.save(any())).willReturn(customerEntity);

    final var customer = customerService.createCustomer(CustomerRequest.builder().build());

    assertThat(customer.getId()).isEqualTo(customerEntity.getId());
    assertThat(customer.getActive()).isEqualTo(customerEntity.getCustomerData().getActive());
    assertThat(customer.getLastname()).isEqualTo(customerEntity.getCustomerData().getLastname());
    assertThat(customer.getFirstname()).isEqualTo(customerEntity.getCustomerData().getFirstname());
    assertThat(customer.getEmail()).isEqualTo(customerEntity.getCustomerData().getEmail());
    assertThat(customer.getRoles()).isEqualTo(customerEntity.getCustomerData().getRoles());
  }

  @Test
  void given_CustomerRequest_when_call_updateCustomer_then_return_customerResponse() {
    final var customerEntity = Instancio.create(CustomerEntity.class);
    final var customerResponse = Instancio.of(CustomerResponse.class)
        .set(field(CustomerResponse::getId), customerEntity.getId())
        .set(field(CustomerResponse::getFirstname), customerEntity.getCustomerData().getFirstname())
        .set(field(CustomerResponse::getLastname), customerEntity.getCustomerData().getLastname())
        .set(field(CustomerResponse::getActive), customerEntity.getCustomerData().getActive())
        .set(field(CustomerResponse::getEmail), customerEntity.getCustomerData().getEmail())
        .set(field(CustomerResponse::getRoles), customerEntity.getCustomerData().getRoles())
        .create();
    given(customerMapper.toCustomerResponse(customerEntity)).willReturn(customerResponse);
    given(customerRepository.save(any())).willReturn(customerEntity);

    final var customer = customerService.updateCustomer(UUID.randomUUID(), CustomerRequest.builder().build());

    assertThat(customer.getId()).isEqualTo(customerEntity.getId());
    assertThat(customer.getActive()).isEqualTo(customerEntity.getCustomerData().getActive());
    assertThat(customer.getLastname()).isEqualTo(customerEntity.getCustomerData().getLastname());
    assertThat(customer.getFirstname()).isEqualTo(customerEntity.getCustomerData().getFirstname());
    assertThat(customer.getEmail()).isEqualTo(customerEntity.getCustomerData().getEmail());
    assertThat(customer.getRoles()).isEqualTo(customerEntity.getCustomerData().getRoles());
  }

  @Test
  void given_CustomerRequest_and_customer_not_found_when_call_updateCustomer_then_return_BusinessError() {
    final var id = UUID.randomUUID();
    final var customerRequest = Instancio.create(CustomerRequest.class);
    given(customerRepository.save(any())).willThrow(new RuntimeException());

    assertThrows(BusinessError.class,
        () -> customerService.updateCustomer(id, customerRequest));
  }

  @Test
  void given_customer_id_when_call_deleteCustomer_then_ok() {
    doNothing().when(customerRepository).deleteById(any());

    customerService.deleteCustomer(UUID.randomUUID());
    verify(customerRepository).deleteById(any());
  }

}
