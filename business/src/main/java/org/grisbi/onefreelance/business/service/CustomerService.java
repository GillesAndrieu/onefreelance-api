package org.grisbi.onefreelance.business.service;

import io.vavr.control.Try;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grisbi.onefreelance.business.mappers.CustomerMapper;
import org.grisbi.onefreelance.model.dto.request.CustomerRequest;
import org.grisbi.onefreelance.model.dto.response.CustomerResponse;
import org.grisbi.onefreelance.model.errors.BusinessError;
import org.grisbi.onefreelance.model.errors.ErrorHandler;
import org.grisbi.onefreelance.persistence.entity.CustomerEntity;
import org.grisbi.onefreelance.persistence.repository.CustomerRepository;
import org.springframework.stereotype.Service;

/**
 * Customer Service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  /**
   * Get customer information by id.
   *
   * @param id of customer
   * @return customer response
   */
  public CustomerResponse getCustomer(final UUID id) {
    final CustomerEntity customerEntity = customerRepository.findById(id)
        .orElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, "Customer Not Found"));
    return customerMapper.toCustomerResponse(customerEntity);
  }

  /**
   * Get information for all customers.
   *
   * @return customer response
   */
  public List<CustomerResponse> getAllCustomers() {
    return customerRepository.findAll()
        .stream()
        .map(customerMapper::toCustomerResponse)
        .toList();
  }

  /**
   * Create new customer.
   *
   * @param customerRequest information
   * @return customer response
   */
  public CustomerResponse createCustomer(final CustomerRequest customerRequest) {
    final CustomerEntity customerEntity = customerRepository
        .save(customerMapper.toCreateCustomerEntity(customerRequest));
    return customerMapper.toCustomerResponse(customerEntity);
  }

  /**
   * Update existing customer.
   *
   * @param id              of customer
   * @param customerRequest information
   * @return customer response
   */
  public CustomerResponse updateCustomer(final UUID id, final CustomerRequest customerRequest) {
    final CustomerEntity customerEntity = Try.of(() -> customerRepository
            .save(customerMapper.toUpdateCustomerEntity(id, customerRequest)))
        .getOrElseThrow(() -> BusinessError.forError(ErrorHandler.NOT_FOUND, "Customer Not Found"));
    return customerMapper.toCustomerResponse(customerEntity);
  }

  /**
   * Delete customer.
   *
   * @param id of customer
   */
  public void deleteCustomer(final UUID id) {
    customerRepository.deleteById(id);
  }
}
