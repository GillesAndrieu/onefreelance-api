package org.grisbi.onefreelance.security.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

  @InjectMocks
  private JwtTokenService jwtTokenService;

  @Test
  void given_valid_token_when_create_token_then_return_email() {
    final var jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiZW1haWwiOiJqb2huLmRvZUBnbWFpbC5jb20iLCJpYXQiOjE1MTYyMzkwMjJ9.oC7b5PCmJuMsIN67i_3qfbX5QBUOeBPusFMtdm9ojFs";

    final var email = jwtTokenService.validateTokenAndGetEmail(jwt);

    assertThat(email).isEqualTo("john.doe@gmail.com");
  }

  @Test
  void given_bad_token_when_create_token_then_throws_exception() {
    final var jwt = "eyJhbGciOiJIUzsI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiZW1haWwiOiJqb2huLmRvZUBnbWFpbC5jb20iLCJpYXQiOjE1MTYyMzkwMjJ9.oC7b5PCmJuMsIN67i_3qfbX5QBUOeBPusFMtdm9ojFs";

    assertThrows(JWTVerificationException.class, () -> jwtTokenService.validateTokenAndGetEmail(jwt));
  }
}
