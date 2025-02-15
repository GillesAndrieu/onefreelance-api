package org.grisbi.onefreelance.security.config;

import lombok.RequiredArgsConstructor;
import org.grisbi.onefreelance.security.service.JwtConverter;
import org.grisbi.onefreelance.security.service.JwtTokenService;
import org.grisbi.onefreelance.security.service.JwtUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Configuration of the security.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtUserDetailsService jwtUserDetailsService;
  private final JwtTokenService jwtTokenService;

  /**
   * The security filter chain.
   *
   * @param http security
   * @return security filter
   * @throws Exception security
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(c ->
            c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(c ->
            c.jwt(j -> j.jwtAuthenticationConverter(new JwtConverter(jwtUserDetailsService, jwtTokenService))))
        .exceptionHandling(customizer ->
            customizer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/actuator/health").permitAll()
            .anyRequest().authenticated()
        );

    return http.build();
  }
}
