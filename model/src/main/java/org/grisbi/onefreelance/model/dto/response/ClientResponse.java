package org.grisbi.onefreelance.model.dto.response;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * The client response DTO.
 */
@Value
@Builder
@Jacksonized
public class ClientResponse {

  UUID id;
  String name;
  AddressResponse address;
  String siret;
  String referent;
  Instant createAt;
  Instant updateAt;
}
