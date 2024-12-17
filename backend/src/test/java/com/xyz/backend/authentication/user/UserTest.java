package com.xyz.backend.authentication.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyz.backend.BackendApplicationTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
public class UserTest extends BackendApplicationTest {

  DashUserDetailsRepository dashUserDetailsRepository;

  @Autowired
  public UserTest(MockMvc restClient, ObjectMapper objectMapper, DashUserDetailsRepository dashUserDetailsRepository) {
    super(restClient, "/", objectMapper);
    this.dashUserDetailsRepository = dashUserDetailsRepository;
  }

  @BeforeAll
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
