package org.grisbi.onefreelance.security.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.UUID;
import org.grisbi.onefreelance.security.dto.JwtUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
class JwtConverterTest {

  @Mock
  private JwtUserDetailsService jwtUserDetailsService;
  @Mock
  private JwtTokenService jwtTokenService;


  @Test
  void given_token_when_jwtConverter_then_return_principal() {
    final String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    given(jwtTokenService.validateTokenAndGetEmail(any())).willReturn("john.doe@gmail.com");
    given(jwtUserDetailsService.loadUserByUsername(any()))
        .willReturn(new JwtUserDetails(UUID.randomUUID(), "test", List.of()));

    final var convert = new JwtConverter(jwtUserDetailsService, jwtTokenService);
    final var yo = convert.convert(Jwt.withTokenValue(jwt)
        .claim("sub", "test")
        .header("alg", "HS256")
        .build());

    assertThat(yo.getPrincipal()).isNotNull();
  }
}
