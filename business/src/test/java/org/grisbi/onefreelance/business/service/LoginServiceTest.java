package org.grisbi.onefreelance.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import org.grisbi.onefreelance.business.mappers.LoginMapper;
import org.grisbi.onefreelance.model.dto.response.LoginResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

  @Mock
  LoginMapper loginMapper;
  @InjectMocks
  private LoginService loginService;

  @Test
  void given_customer_id_and_authority_when_call_getLoginRoles_then_return_loginResponse() {
    final UUID id = UUID.randomUUID();
    final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

    when(loginMapper.toLoginResponse(any(), any())).thenReturn(LoginResponse.builder()
        .id(id)
        .roles(List.of("ROLE_USER"))
        .build());

    final LoginResponse response = loginService.getLoginRoles(id, authorities);
    assertThat(response.getId()).isEqualTo(id);
    assertThat(response.getRoles()).isEqualTo(List.of("ROLE_USER"));
  }
}
