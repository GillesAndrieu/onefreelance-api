package org.grisbi.onefreelance.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Roles.
 */
@Getter
@RequiredArgsConstructor
public enum Role {
  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_USER("ROLE_USER"),
  ROLE_ANONYMOUS("ROLE_ANONYMOUS");

  public final String roleName;
}
