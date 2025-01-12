package com.xyz.backend.dashboard;

import com.xyz.backend.dashboard.dtos.DashboardDTO;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

  private DashboardService dashboardService;

  @PutMapping
  public ResponseEntity<DashboardDTO> createDashboard() {
    return dashboardService.createDashboard();
  }

  @PostMapping
  public ResponseEntity<DashboardDTO> updateDashboard(@RequestBody DashboardDTO dashboardDTO) {
    return dashboardService.editDashboard(dashboardDTO);
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteDashboard(@RequestBody DashboardDTO dashboardDTO) {
    return dashboardService.deleteDashboard(dashboardDTO);
  }

  @GetMapping
  public ResponseEntity<DashboardDTO> loadDashboard(@RequestParam("uuid") String uuid) {
    return dashboardService.loadDashboard(UUID.fromString(uuid));
  }

  @GetMapping("/all")
  public ResponseEntity<DashboardDTO[]> loadAllDashboards() {
    return dashboardService.loadAllDashboards();
  }
}
