package org.grisbi.onefreelance.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.grisbi.onefreelance.business.service.LoginService;
import org.grisbi.onefreelance.model.dto.response.LoginResponse;
import org.grisbi.onefreelance.security.dto.JwtUserDetails;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/login")
@RequiredArgsConstructor
@Validated
@Tag(name = "Login", description = "Use these API with JWT for all customer action")
public class LoginController {

  private final LoginService loginService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<LoginResponse> getLogin(final Authentication authentication) {
    final JwtUserDetails user = (JwtUserDetails) authentication.getPrincipal();
    return ResponseEntity.ok(loginService.getLoginRoles(user.getId(), authentication.getAuthorities()));
  }
}
