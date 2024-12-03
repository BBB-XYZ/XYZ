package com.xyz.backend.authentication.user;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface DashUserDetailsRepository extends CrudRepository<DashUserDetails, Long> {
  Optional<DashUserDetails> findByUsername(String username);

  Optional<DashUserDetails> findBySession_Token(String sessionToken);
}