package com.xyz.backend.authentication;

import com.xyz.backend.authentication.user.DashUserDetails;
import com.xyz.backend.authentication.user.DashUserDetailsRepository;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DashAuthenticationManager implements AuthenticationManager {
  private DashUserDetailsRepository userDetailsRepository;
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    Optional<DashUserDetails> userDetails = userDetailsRepository.findByUsername(username);
    if (userDetails.isEmpty() || !passwordEncoder.matches(password, userDetails.get().getPassword())) {
      throw new AuthenticationException("Invalid credentials") {};
    }

    return new UsernamePasswordAuthenticationToken(userDetails.get(), null, userDetails.get().getAuthorities());
  }
}