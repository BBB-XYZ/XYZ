package com.xyz.backend.authentication.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.xyz.backend.BackendApplicationTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest extends BackendApplicationTest {

  DashUserDetailsRepository dashUserDetailsRepository;

  @BeforeEach
  public void mock() {
    DashUserDetails existing = new DashUserDetails();
    existing.setUsername("existing");
    existing.setPassword("existing");

    dashUserDetailsRepository.save(existing);
  }

  @Test
  void testCreateUser() {
    DashUserDetails userDetails = new DashUserDetails();
    userDetails.setUsername("unavailable");
    userDetails.setPassword("unavailable");
    dashUserDetailsRepository.save(userDetails);

    assertTrue(dashUserDetailsRepository.findByUsername("unavailable").isPresent());
  }

  @Test
  void testUpdateUser() {
    Optional<DashUserDetails> userDetails = dashUserDetailsRepository.findByUsername("existing");
    userDetails.ifPresent(user -> {
      user.setPassword("updated");
      dashUserDetailsRepository.save(user);
    });

    userDetails = dashUserDetailsRepository.findByUsername("existing");

    assertTrue(userDetails.isPresent());
    assertEquals("updated", userDetails.get().getPassword());
  }

  @Test
  void testDeleteUser() {
    Optional<DashUserDetails> userDetails = dashUserDetailsRepository.findByUsername("existing");
    userDetails.ifPresent(user -> dashUserDetailsRepository.delete(user));

    assertTrue(dashUserDetailsRepository.findByUsername("existing").isEmpty());
  }

  @Test
  void testGetUser() {
    Optional<DashUserDetails> userDetails = dashUserDetailsRepository.findByUsername("existing");

    assertTrue(userDetails.isPresent());
    assertEquals("existing", userDetails.get().getUsername());
  }
}
