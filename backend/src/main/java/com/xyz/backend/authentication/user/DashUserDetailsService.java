package com.xyz.backend.authentication.user;

import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class DashUserDetailsService implements UserDetailsService {
  private DashUserDetailsRepository userDetailsRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userDetailsRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found [%s]".formatted(username)));
  }
}