package com.xyz.backend.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyz.backend.BackendApplicationTest;
import com.xyz.backend.authentication.dtos.LoginRequestDTO;
import com.xyz.backend.authentication.session.UserSessionEntity;
import com.xyz.backend.authentication.session.UserSessionRepository;
import com.xyz.backend.authentication.user.DashUserDetails;
import com.xyz.backend.authentication.user.DashUserDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
public class AuthTest extends BackendApplicationTest {
  private AuthenticationService authenticationService;
  private DashUserDetailsRepository userDetailsRepository;
  private UserSessionRepository userSessionRepository;
  private PasswordEncoder passwordEncoder;

  private static final String USERNAME = "valid";
  private static final String PASSWORD = "valid_password";

  private String validSession;

  @Autowired
  public AuthTest(MockMvc restClient, ObjectMapper objectMapper) {
    super(restClient, "/api/login", objectMapper);
  }

  @BeforeEach
  void before() {
    DashUserDetails dashUserDetails = new DashUserDetails();
    dashUserDetails.setUsername(USERNAME);
    dashUserDetails.setPassword(passwordEncoder.encode(PASSWORD));

    userDetailsRepository.save(dashUserDetails);

    UserSessionEntity userSessionEntity = new UserSessionEntity();
    userSessionEntity.setExpiresAt(System.currentTimeMillis() + (1000 * 3600));
    validSession = userSessionRepository.save(userSessionEntity).getToken();
  }

  @Test
  void testLogin() throws Exception {
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO(USERNAME, PASSWORD);
    assertEquals(200, authenticationService.login(loginRequestDTO).getStatusCode().value());
  }

  @Test
  void testLogoutSuccessful() throws Exception {
    testEndpoint(HttpMethod.POST, validSession, null, result -> {
      assertEquals(200, result.getResponse().getStatus());
    });
  }

  @Test
  void testLogoutInvalidSession() throws Exception {
    testEndpoint(HttpMethod.POST, "invalid", null, result -> {
      assertEquals(401, result.getResponse().getStatus());
    });
  }

  @Test
  void testRegisterSuccessful() {
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO("new", "new_password");
    assertEquals(200, authenticationService.register(loginRequestDTO).getStatusCode().value());
  }

  @Test
  void testRegisterExistingUser() {
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO(USERNAME, PASSWORD);
    assertEquals(409, authenticationService.register(loginRequestDTO).getStatusCode().value());
  }
}
