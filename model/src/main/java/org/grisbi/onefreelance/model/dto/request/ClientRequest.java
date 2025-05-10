package org.grisbi.onefreelance.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * The client request DTO.
 */
@Value
@Builder
@Jacksonized
public class ClientRequest {

  @NotNull
  String name;
  AddressRequest address;
  @NotNull
  String siret;
  @NotNull
  String referent;
}
