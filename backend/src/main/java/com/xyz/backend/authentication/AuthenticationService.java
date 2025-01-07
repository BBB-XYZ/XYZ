package com.xyz.backend.authentication;

import com.xyz.backend.authentication.dtos.LoginRequestDTO;
import com.xyz.backend.authentication.session.UserSessionEntity;
import com.xyz.backend.authentication.session.UserSessionService;
import com.xyz.backend.authentication.session.dtos.SessionDTO;
import com.xyz.backend.authentication.user.DashUserDetails;
import com.xyz.backend.authentication.user.DashUserDetailsRepository;
import com.xyz.backend.dashboard.DashboardService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
  private DashAuthenticationManager authenticationManager;
  private DashUserDetailsRepository userDetailsRepository;
  private DashboardService dashboardService;

  private UserSessionService userSessionService;

  private PasswordEncoder passwordEncoder;

  public ResponseEntity<SessionDTO> login(LoginRequestDTO loginRequest) {
    Authentication authenticationRequest = UsernamePasswordAuthenticationToken
        .unauthenticated(loginRequest.username(), loginRequest.password());
    Authentication authentication = authenticationManager.authenticate(authenticationRequest);

    if (authentication.isAuthenticated()) {
      SecurityContextHolder.getContext().setAuthentication(authentication);
      Optional<DashUserDetails> userDetails = userDetailsRepository.findByUsername(authentication.getName());

      if (userDetails.isEmpty()) {
        return ResponseEntity.status(401).build();
      }

      UserSessionEntity userSessionEntity = userSessionService.createSession();

      userDetails.get().setSession(userSessionEntity);
      userDetailsRepository.save(userDetails.get());

      SessionDTO sessionDto = new SessionDTO(userSessionEntity.getToken());
      return ResponseEntity.ok().body(sessionDto);
    }

    return ResponseEntity.status(401).build();
  }

  public ResponseEntity<SessionDTO> register(LoginRequestDTO loginRequest) {
    DashUserDetails userDetails = new DashUserDetails();
    userDetails.setUsername(loginRequest.username());
    userDetails.setPassword(passwordEncoder.encode(loginRequest.password()));

    if (userDetailsRepository.findByUsername(loginRequest.username()).isPresent()) {
      return ResponseEntity.status(409).build();
    }

    userDetailsRepository.save(userDetails);

    ResponseEntity<SessionDTO> loginResponse = login(loginRequest);

    if (!loginResponse.getStatusCode().isError()) {
      dashboardService.createDashboard();
    }

    return loginResponse;
  }

  public ResponseEntity<Void> logout() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Optional<DashUserDetails> userDetails = userDetailsRepository.findByUsername(authentication.getName());

    if (userDetails.isEmpty()) {
      return ResponseEntity.status(401).build();
    }

    UserSessionEntity session = userDetails.get().getSession();

    userDetails.get().setSession(null);
    userDetailsRepository.save(userDetails.get());
    userSessionService.invalidateSession(session.getToken());

    return ResponseEntity.ok().build();
  }
}