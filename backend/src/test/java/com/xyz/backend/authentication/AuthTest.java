package com.xyz.backend.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyz.backend.BackendApplicationTest;
import com.xyz.backend.authentication.dtos.LoginRequestDTO;
import com.xyz.backend.authentication.session.UserSessionEntity;
import com.xyz.backend.authentication.session.UserSessionRepository;
import com.xyz.backend.authentication.session.dtos.SessionDTO;
import com.xyz.backend.authentication.user.DashUserDetails;
import com.xyz.backend.authentication.user.DashUserDetailsRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
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
  public AuthTest(MockMvc restClient, ObjectMapper objectMapper, AuthenticationService authenticationService,
      DashUserDetailsRepository userDetailsRepository, UserSessionRepository userSessionRepository,
      PasswordEncoder passwordEncoder) {
    super(restClient, "/logout", objectMapper);

    this.authenticationService = authenticationService;
    this.userDetailsRepository = userDetailsRepository;
    this.userSessionRepository = userSessionRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @BeforeAll
  void before() {
    userDetailsRepository.deleteAll();

    DashUserDetails dashUserDetails = new DashUserDetails();
    dashUserDetails.setUsername(USERNAME);
    dashUserDetails.setPassword(passwordEncoder.encode(PASSWORD));

    UserSessionEntity userSessionEntity = new UserSessionEntity();
    userSessionEntity.setExpiresAt(System.currentTimeMillis() + (1000 * 3600));
    validSession = userSessionRepository.save(userSessionEntity).getToken();

    dashUserDetails.setSession(userSessionEntity);
    userDetailsRepository.save(dashUserDetails);
  }

  @Test
  void testLogin() throws Exception {
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO(USERNAME, PASSWORD);
    ResponseEntity<SessionDTO> response = authenticationService.login(loginRequestDTO);
    assertNotNull(response.getBody());

    validSession = response.getBody().getToken();
    assertEquals(200, response.getStatusCode().value());
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
