package com.xyz.backend.authentication.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService {
  @Value("${backend.session.expiration}")
  private long sessionExpiration;

  private UserSessionRepository userSessionRepository;

  public UserSessionService(UserSessionRepository userSessionRepository) {
    this.userSessionRepository = userSessionRepository;
  }

  public UserSessionEntity createSession() {
    UserSessionEntity session = new UserSessionEntity();
    session.setExpiresAt(System.currentTimeMillis() + (1000 * sessionExpiration));
    return userSessionRepository.save(session);
  }

  public void invalidateSession(String token) {
    userSessionRepository.deleteById(token);
  }
}