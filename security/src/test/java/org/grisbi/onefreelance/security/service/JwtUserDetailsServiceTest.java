package org.grisbi.onefreelance.security.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.grisbi.onefreelance.persistence.entity.CustomerEntity;
import org.grisbi.onefreelance.persistence.repository.CustomerRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class JwtUserDetailsServiceTest {

  @InjectMocks
  private JwtUserDetailsService userDetailsService;

  @Mock
  private CustomerRepository customerRepository;

  @Test
  void given_customer_when_loadUserByUsername_then_return_userDetails() {
    final var customer = Instancio.create(CustomerEntity.class);
    given(customerRepository.findByProfileEmail(anyString())).willReturn(Optional.of(customer));

    final var userDetails = userDetailsService.loadUserByUsername("john.doe@gmail.com");
    assertThat(userDetails.id).isEqualTo(customer.getId());
  }

  @Test
  void given_customer_not_found_when_loadUserByUsername_then_throws_exception() {
    given(customerRepository.findByProfileEmail(anyString())).willReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class,
        () -> userDetailsService.loadUserByUsername("john.doe@gmail.com"));
  }
}
