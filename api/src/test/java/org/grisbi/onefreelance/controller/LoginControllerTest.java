package org.grisbi.onefreelance.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;
import org.grisbi.onefreelance.api.controller.ControllerExceptionTranslator;
import org.grisbi.onefreelance.api.controller.LoginController;
import org.grisbi.onefreelance.business.service.LoginService;
import org.grisbi.onefreelance.model.config.JacksonConfig;
import org.grisbi.onefreelance.model.dto.response.LoginResponse;
import org.grisbi.onefreelance.security.dto.JwtUserDetails;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@EnableMethodSecurity
@WebMvcTest(controllers = {LoginController.class})
@ContextConfiguration(classes = { LoginController.class, ControllerExceptionTranslator.class, JacksonConfig.class })
class LoginControllerTest {

  @MockitoBean
  LoginService loginService;
  @Autowired
  private MockMvc mockMvc;

  @Test
  void given_customer_id_when_call_getCustomer_then_return_200_and_CustomerResponse() throws Exception {
    final var authentication = new UsernamePasswordAuthenticationToken(new JwtUserDetails(UUID.randomUUID(),
        "username", List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))), "password",
        List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    final var loginResponse = Instancio.create(LoginResponse.class);
    given(loginService.getLoginRoles(any(), any())).willReturn(loginResponse);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/login")
            .with(authentication(authentication))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(loginService).getLoginRoles(any(), any());
  }

}
