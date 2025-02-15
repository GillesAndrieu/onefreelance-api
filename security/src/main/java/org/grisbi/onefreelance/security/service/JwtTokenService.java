package org.grisbi.onefreelance.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Jwt token service.
 */
@Slf4j
@Service
public class JwtTokenService {

  public static final String EMAIL_TOKEN_FIELD = "email";

  /**
   * Check the token validity and claim the email.
   *
   * @param token google auth
   * @return the email
   */
  public String validateTokenAndGetEmail(final String token) {
    return Try.of(() -> JWT.decode(token).getClaim(EMAIL_TOKEN_FIELD).asString())
        .getOrElseThrow(ex -> new JWTVerificationException("Invalid JWT token : %s", ex));
  }
}
