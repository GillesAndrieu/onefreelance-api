package org.grisbi.onefreelance.security.dto;

import java.util.Collection;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Generate UserDetails.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class JwtUserDetails extends User {

  public final UUID id;

  public JwtUserDetails(final UUID id, final String username,
                        Collection<? extends GrantedAuthority> authorities) {
    super(username, "", authorities);
    this.id = id;
  }
}
