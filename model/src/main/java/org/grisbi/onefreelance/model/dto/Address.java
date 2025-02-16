package org.grisbi.onefreelance.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Address data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

  private String number;
  private String street;
  private String city;
  private String postalCode;
}
