package org.grisbi.onefreelance.model.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * The customer response DTO.
 */
@Value
@Builder
@Jacksonized
public class CustomerResponse {

  UUID id;
  String firstname;
  String lastname;
  String email;
  List<String> roles;
  Boolean active;
  Instant createAt;
  Instant updateAt;
}
