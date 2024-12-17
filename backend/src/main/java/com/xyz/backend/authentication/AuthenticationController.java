package com.xyz.backend.authentication;

import com.xyz.backend.authentication.dtos.LoginRequestDTO;
import com.xyz.backend.authentication.session.dtos.SessionDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {
  private AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<SessionDTO> login(@RequestBody LoginRequestDTO loginRequest) {
    return authenticationService.login(loginRequest);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    return authenticationService.logout();
  }

  @PostMapping("/register")
  public ResponseEntity<SessionDTO> register(@RequestBody LoginRequestDTO loginRequest) {
    return authenticationService.register(loginRequest);
  }
}
