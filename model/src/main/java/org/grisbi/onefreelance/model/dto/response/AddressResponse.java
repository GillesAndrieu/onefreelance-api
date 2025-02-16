package org.grisbi.onefreelance.model.dto.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * The client response DTO.
 */
@Value
@Builder
@Jacksonized
public class AddressResponse {

  String number;
  String street;
  String city;
  String postalCode;
}
