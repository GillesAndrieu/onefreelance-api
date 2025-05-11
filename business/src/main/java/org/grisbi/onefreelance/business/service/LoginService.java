package org.grisbi.onefreelance.business.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.grisbi.onefreelance.business.mappers.LoginMapper;
import org.grisbi.onefreelance.model.dto.response.LoginResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * Customer Service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

  private final LoginMapper loginMapper;

  /**
   * Get the login roles.
   *
   * @param id          of customer
   * @param authorities of customer
   * @return LoginResponse
   */
  public LoginResponse getLoginRoles(final UUID id, Collection<? extends GrantedAuthority> authorities) {
    final List<String> roles = authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .toList();
    return loginMapper.toLoginResponse(id, roles);
  }
}
