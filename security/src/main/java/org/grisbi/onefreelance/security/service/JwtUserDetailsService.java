package org.grisbi.onefreelance.security.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.grisbi.onefreelance.persistence.entity.CustomerEntity;
import org.grisbi.onefreelance.persistence.repository.CustomerRepository;
import org.grisbi.onefreelance.security.dto.JwtUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetails service.
 */
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

  private final CustomerRepository customerRepository;

  /**
   * Search the user information and create the userDetails with roles.
   *
   * @param email the email identifying the user whose data is required
   * @return user details
   * @throws UsernameNotFoundException if not created
   */
  @Override
  public JwtUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    final CustomerEntity customer = customerRepository.findByProfileEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email));
    final List<SimpleGrantedAuthority> roles = customer.getProfile()
        .getRoles()
        .stream()
        .map(SimpleGrantedAuthority::new)
        .toList();
    return new JwtUserDetails(customer.getId(), email, roles);
  }
}
