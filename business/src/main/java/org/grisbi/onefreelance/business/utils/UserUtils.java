package org.grisbi.onefreelance.business.utils;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.grisbi.onefreelance.security.dto.JwtUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * User utils.
 */
@UtilityClass
public class UserUtils {

  public UUID getConnectedUser() {
    return ((JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
  }
}
