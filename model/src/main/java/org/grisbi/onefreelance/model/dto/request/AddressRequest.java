package org.grisbi.onefreelance.model.dto.request;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * Address request DTO.
 */
@Value
@Builder
@Jacksonized
public class AddressRequest {

  String number;
  String street;
  String city;
  String postalCode;
}
