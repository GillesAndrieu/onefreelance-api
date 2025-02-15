package org.grisbi.onefreelance.security.service;

import lombok.RequiredArgsConstructor;
import org.grisbi.onefreelance.security.dto.JwtUserDetails;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Jwt converter to adding grant authorities after jwt validate by google.
 */
@Service
@RequiredArgsConstructor
public class JwtConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

  private final JwtUserDetailsService jwtUserDetailsService;
  private final JwtTokenService jwtTokenService;

  @Override
  public UsernamePasswordAuthenticationToken convert(Jwt source) {
    final String email = jwtTokenService.validateTokenAndGetEmail(source.getTokenValue());
    final JwtUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(email);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}
