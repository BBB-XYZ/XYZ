package com.xyz.backend.authentication.session;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserSessionRepository extends CrudRepository<UserSessionEntity, String> {
  Optional<UserSessionEntity> findByToken(String token);
}