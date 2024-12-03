package com.xyz.backend.authentication;

import com.xyz.backend.authentication.dtos.LoginRequestDto;
import com.xyz.backend.authentication.session.dtos.SessionDto;
import com.xyz.backend.dtos.GenericMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthenticationController {
  private AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<SessionDto> login(@RequestBody LoginRequestDto loginRequest) {
    return authenticationService.login(loginRequest);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    return ResponseEntity.ok().build();
  }

  @PostMapping("/register")
  public ResponseEntity<GenericMessageDto> register(@RequestBody LoginRequestDto loginRequest) {
    return authenticationService.register(loginRequest);
  }
}
