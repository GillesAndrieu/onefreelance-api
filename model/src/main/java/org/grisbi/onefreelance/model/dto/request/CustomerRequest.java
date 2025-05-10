package org.grisbi.onefreelance.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  String firstname;
  @NotNull
  String lastname;
  @Email
  @NotNull
  String email;
  @Builder.Default
  List<String> roles = List.of();
  @Builder.Default
  Boolean active = false;
}
