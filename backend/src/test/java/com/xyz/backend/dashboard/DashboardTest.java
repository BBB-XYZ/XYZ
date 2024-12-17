package com.xyz.backend.dashboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyz.backend.BackendApplicationTest;
import com.xyz.backend.TestDataService;
import com.xyz.backend.authentication.dashboard.DashboardEntity;
import com.xyz.backend.authentication.dashboard.DashboardRepository;
import com.xyz.backend.authentication.dashboard.DashboardService;
import com.xyz.backend.authentication.dashboard.dtos.DashboardDTO;
import com.xyz.backend.authentication.dashboard.widget.WidgetEntity;
import com.xyz.backend.authentication.dashboard.widget.dtos.WidgetDTO;
import com.xyz.backend.authentication.user.DashUserDetails;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
public class DashboardTest extends BackendApplicationTest {

  private DashboardService dashboardService;
  private TestDataService testDataService;
  private DashboardRepository dashboardRepository;

  private static final String USERNAME = "valid";
  private static final String PASSWORD = "valid_password";

  private DashUserDetails currentUser = null;
  private DashboardEntity existingDashboard = null;

  public DashboardTest(MockMvc mockMvc, String endpoint,
      ObjectMapper objectMapper) {
    super(mockMvc, endpoint, objectMapper);
  }

  @BeforeAll
  void beforeAll() {
    currentUser = testDataService.authenticateSession(USERNAME, PASSWORD);

    DashboardDTO dashboardDTO = dashboardService.createDashboard().getBody();
    existingDashboard = dashboardRepository.findById(UUID.fromString(dashboardDTO.getUuid()))
        .orElseThrow(() -> new IllegalStateException("Dashboard cannot be null"));
  }

  @Test
  void testCreateDashboardSuccessful() {
    ResponseEntity<DashboardDTO> response = dashboardService.createDashboard();
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  void testLoadAllDashboards() {
    ResponseEntity<DashboardDTO[]> response = dashboardService.loadAllDashboards();
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  void testUpdateDashboard() {
    DashboardDTO updatedDashboard = new DashboardDTO(existingDashboard.getUuid().toString(),
        "new-name", existingDashboard.getOwner().toDTO(), existingDashboard.getWidgets().stream()
        .map(WidgetEntity::toDTO).toArray(WidgetDTO[]::new));
    // TODO: Update data

    ResponseEntity<DashboardDTO> response = dashboardService.editDashboard(updatedDashboard);
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  void testDeleteDashboard() {
    ResponseEntity<Void> response = dashboardService.deleteDashboard(existingDashboard.toDTO());
    assertEquals(200, response.getStatusCode().value());
  }
}
