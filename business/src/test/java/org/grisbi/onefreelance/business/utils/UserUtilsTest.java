package org.grisbi.onefreelance.business.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.grisbi.onefreelance.security.dto.JwtUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class UserUtilsTest {

  @Test
  void when_call_getConnectedUser_return_connected_uuid() {
    final var id = UUID.randomUUID();
    createSecurityContext(id);

    final var user = UserUtils.getConnectedUser();

    assertThat(user).isEqualTo(id);
  }

  private void createSecurityContext(UUID id) {
    SecurityContextHolder.createEmptyContext();
    SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("user",
        new JwtUserDetails(id, "username", List.of(new SimpleGrantedAuthority("ROLE_USER"))),
        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
  }
}
