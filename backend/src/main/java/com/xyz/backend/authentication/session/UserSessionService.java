package com.xyz.backend.authentication.session;

import com.xyz.backend.authentication.user.DashUserDetails;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService {
  @Value("${backend.session.expiration}")
  private long sessionExpiration;

  public UserSessionEntity createSession(DashUserDetails userDetails) {
    String token = UUID.randomUUID().toString();
    UserSessionEntity session = new UserSessionEntity();
    session.setToken(token);
    session.setExpiresAt(System.currentTimeMillis() + (1000 * sessionExpiration));
    userDetails.setSession(session);

    return session;
  }
}