package com.xyz.backend;

import com.xyz.backend.authentication.AuthenticationService;
import com.xyz.backend.authentication.dtos.LoginRequestDTO;
import com.xyz.backend.authentication.user.DashUserDetails;
import com.xyz.backend.authentication.user.DashUserDetailsRepository;
import com.xyz.backend.dashboard.DashboardEntity;
import com.xyz.backend.dashboard.DashboardRepository;
import java.util.Optional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@NoArgsConstructor
public class TestDataService {

  private AuthenticationService authenticationService;
  private DashUserDetailsRepository userDetailsRepository;
  private DashboardRepository dashboardRepository;

  @Autowired
  public TestDataService(AuthenticationService authenticationService,
      DashUserDetailsRepository userDetailsRepository,
      DashboardRepository dashboardRepository) {
    this.authenticationService = authenticationService;
    this.userDetailsRepository = userDetailsRepository;
    this.dashboardRepository = dashboardRepository;
  }

  public DashUserDetails authenticateSession(String username, String password) {
    LoginRequestDTO loginRequestDTO = new LoginRequestDTO(username, password);
    DashUserDetails userDetails = login(loginRequestDTO);

    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,
            userDetails.getSession().getToken(), userDetails.getAuthorities()));

    return userDetails;
  }

  private DashUserDetails login(LoginRequestDTO loginRequestDTO) {
    boolean exists = userDetailsRepository.existsByUsername(
        loginRequestDTO.username());

    if (exists) {
      authenticationService.login(loginRequestDTO);
      return userDetailsRepository.findByUsername(loginRequestDTO.username())
          .orElseThrow(() -> new RuntimeException("User not found [%s]".formatted(loginRequestDTO.username())));
    }

    authenticationService.register(loginRequestDTO);
    return userDetailsRepository.findByUsername(loginRequestDTO.username()).orElseThrow(
        () -> new RuntimeException("User not found [%s]".formatted(loginRequestDTO.username())));
  }

  public DashUserDetails createUser(String username, String password) {
    Optional<DashUserDetails> userDetailsOptional = userDetailsRepository.findByUsername(username);

    if (userDetailsOptional.isPresent()) {
      return userDetailsOptional.get();
    }

    DashUserDetails userDetails = new DashUserDetails();
    userDetails.setUsername(username);
    userDetails.setPassword(password);

    userDetailsRepository.save(userDetails);
    return userDetails;
  }

  public DashboardEntity createDashboard(String username) {
    DashUserDetails dashUserDetails = userDetailsRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found [%s]".formatted(username)));

    DashboardEntity dashboardEntity = new DashboardEntity();
    //dashboardEntity.setOwner(dashUserDetails);

    return dashboardRepository.save(dashboardEntity);
  }
}