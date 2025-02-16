package org.grisbi.onefreelance.model.dto.request;

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

  String name;
  AddressRequest address;
  String siret;
  String referent;
}
