package org.grisbi.onefreelance.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.grisbi.onefreelance.api.swagger.customer.DeleteCustomerDocumentation;
import org.grisbi.onefreelance.api.swagger.customer.GetAllCustomersDocumentation;
import org.grisbi.onefreelance.api.swagger.customer.GetCustomerDocumentation;
import org.grisbi.onefreelance.api.swagger.customer.PatchCustomerDocumentation;
import org.grisbi.onefreelance.api.swagger.customer.PostCustomerDocumentation;
import org.grisbi.onefreelance.business.service.CustomerService;
import org.grisbi.onefreelance.model.dto.request.CustomerRequest;
import org.grisbi.onefreelance.model.dto.response.CustomerResponse;
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
 * Customer rest controller.
 */
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/v1/customer")
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer", description = "Use these API with JWT for all customer action")
public class CustomerController {

  private final CustomerService customerService;

  /**
   * Get information of all customers.
   *
   * @return all customers
   */
  @GetAllCustomersDocumentation
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
    return ResponseEntity.ok(customerService.getAllCustomers());
  }

  /**
   * Get customer information.
   *
   * @param id of customer
   * @return customer information
   */
  @GetCustomerDocumentation
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomerResponse> getCustomer(@PathVariable final UUID id) {
    return ResponseEntity.ok(customerService.getCustomer(id));
  }

  /**
   * Create new customer.
   *
   * @param customerRequest information
   * @return new customer information
   */
  @PostCustomerDocumentation
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomerResponse> postCustomer(@Valid @RequestBody final CustomerRequest customerRequest) {
    return ResponseEntity.created(URI.create("/v1/customer"))
        .body(customerService.createCustomer(customerRequest));
  }

  /**
   * Update customer.
   *
   * @param id              of customer
   * @param customerRequest information
   * @return customer updated
   */
  @PatchCustomerDocumentation
  @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomerResponse> patchCustomer(
      @PathVariable final UUID id,
      @Valid @RequestBody final CustomerRequest customerRequest) {
    return ResponseEntity.ok(customerService.updateCustomer(id, customerRequest));
  }

  /**
   * Delete customer.
   *
   * @param id of customer
   * @return 204 no content
   */
  @DeleteCustomerDocumentation
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCustomer(
      @PathVariable final UUID id) {
    customerService.deleteCustomer(id);
    return ResponseEntity
        .status(HttpStatusCode.valueOf(HttpStatus.NO_CONTENT.value()))
        .build();
  }
}
