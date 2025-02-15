package org.grisbi.onefreelance.model.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * The customer request DTO.
 */
@Value
@Builder
@Jacksonized
public class CustomerRequest {

  String firstname;
  String lastname;
  String email;
  @Builder.Default
  List<String> roles = List.of();
  Boolean active;
}
