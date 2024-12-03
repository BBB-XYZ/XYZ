package com.xyz.backend.authentication;

import com.xyz.backend.authentication.dtos.LoginRequestDto;
import com.xyz.backend.authentication.session.UserSessionEntity;
import com.xyz.backend.authentication.session.UserSessionRepository;
import com.xyz.backend.authentication.session.UserSessionService;
import com.xyz.backend.authentication.session.dtos.SessionDto;
import com.xyz.backend.authentication.user.DashUserDetails;
import com.xyz.backend.authentication.user.DashUserDetailsRepository;
import com.xyz.backend.dtos.GenericMessageDto;
import java.util.Optional;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class AuthenticationService {
  private DashAuthenticationManager authenticationManager;
  private DashUserDetailsRepository userDetailsRepository;
  private UserSessionRepository userSessionRepository;

  private UserSessionService userSessionService;

  private PasswordEncoder passwordEncoder;
  private HttpSessionSecurityContextRepository securityContextRepository;

  public ResponseEntity<SessionDto> login(LoginRequestDto loginRequest) {
    Authentication authenticationRequest = UsernamePasswordAuthenticationToken
        .unauthenticated(loginRequest.username(), loginRequest.password());
    Authentication authentication = authenticationManager.authenticate(authenticationRequest);

    if (authentication.isAuthenticated()) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
      Optional<DashUserDetails> userDetails = userDetailsRepository.findByUsername(authentication.getName());

      if (userDetails.isEmpty()) {
        return ResponseEntity.status(401).build();
      }

      UserSessionEntity userSessionEntity = userSessionService.createSession(userDetails.get());
      userDetailsRepository.save(userDetails.get());
      return ResponseEntity.ok().header("Set-Cookie", "JSESSIONID=" + userSessionEntity.getToken()).build();
    }

    return ResponseEntity.status(401).build();
  }

  public ResponseEntity<GenericMessageDto> register(LoginRequestDto loginRequest) {
    DashUserDetails userDetails = new DashUserDetails();
    userDetails.setUsername(loginRequest.username());
    userDetails.setPassword(passwordEncoder.encode(loginRequest.password()));

    if (userDetailsRepository.findByUsername(loginRequest.username()).isPresent()) {
      return ResponseEntity.status(409).body(new GenericMessageDto("User already exists"));
    }

    userDetailsRepository.save(userDetails);
    return ResponseEntity.ok().build();
  }
}