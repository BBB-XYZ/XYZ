package com.xyz.backend.dashboard;

import com.xyz.backend.authentication.user.DashUserDetails;
import com.xyz.backend.authentication.user.DashUserDetailsRepository;
import com.xyz.backend.dashboard.dtos.DashboardDTO;
import com.xyz.backend.dashboard.widget.WidgetEntity;
import com.xyz.backend.dashboard.widget.WidgetRepository;
import com.xyz.backend.dashboard.widget.dtos.WidgetDTO;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DashboardService {
  private DashUserDetailsRepository dashUserDetailsRepository;
  private DashboardRepository dashboardRepository;
  private WidgetRepository widgetRepository;

  public ResponseEntity<DashboardDTO[]> loadAllDashboards() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!(authentication.getPrincipal() instanceof DashUserDetails userDetails)) {
      return ResponseEntity.status(401).build();
    }

    DashboardDTO[] dashboardDTOs = dashboardRepository.findAllByOwner(userDetails)
        .stream().map(DashboardEntity::toDTO).toArray(DashboardDTO[]::new);

    return ResponseEntity.ok(dashboardDTOs);
  }

  public ResponseEntity<DashboardDTO> loadDashboard(UUID uuid) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!(authentication.getPrincipal() instanceof DashUserDetails userDetails)) {
      return ResponseEntity.status(401).build();
    }

    Optional<DashboardEntity> dashboardEntity = dashboardRepository.findById(uuid);

    if (dashboardEntity.isEmpty() || !dashboardEntity.get().getOwner().equals(userDetails)) {
      return ResponseEntity.status(401).build();
    }

    return ResponseEntity.ok(dashboardEntity.get().toDTO());
  }

  public ResponseEntity<DashboardDTO> createDashboard() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!(authentication.getPrincipal() instanceof DashUserDetails userDetails)) {
      return ResponseEntity.status(401).build();
    }

    DashboardEntity dashboardEntity = new DashboardEntity();
    dashboardEntity.setOwner(userDetails);
    dashboardEntity.setName("New Dashboard");

    return ResponseEntity.ok(dashboardRepository.save(dashboardEntity).toDTO());
  }

  public ResponseEntity<DashboardDTO> editDashboard(DashboardDTO dashboardDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!(authentication.getPrincipal() instanceof DashUserDetails userDetails)) {
      return ResponseEntity.status(401).build();
    }

    UUID dashboardUuid = UUID.fromString(dashboardDTO.getUuid());
    Optional<DashboardEntity> dashboardEntity = dashboardRepository.findById(dashboardUuid);

    if (dashboardEntity.isEmpty() || !dashboardEntity.get().getOwner().getId().equals(userDetails.getId())) {
      return ResponseEntity.status(404).build();
    }

    DashboardEntity dashboard = dashboardEntity.get();
    dashboard.setName(dashboardDTO.getName());
    dashboard.getWidgets().clear();

    widgetRepository.deleteAllByDashboard(dashboard);

    for (WidgetDTO widgetDTO : dashboardDTO.getWidgets()) {
      WidgetEntity widgetEntity = new WidgetEntity();

      widgetEntity.setDashboard(dashboard);
      widgetEntity.setX(widgetDTO.getX());
      widgetEntity.setY(widgetDTO.getY());
      widgetEntity.setWidth(widgetDTO.getWidth());
      widgetEntity.setHeight(widgetDTO.getHeight());
      widgetEntity.setWidgetStoreUrl(widgetDTO.getWidgetStoreUrl());
      widgetEntity.setWidgetId(widgetDTO.getWidgetId());

      widgetRepository.save(widgetEntity);
      dashboard.getWidgets().add(widgetEntity);
    }

    return ResponseEntity.ok(dashboardRepository.save(dashboard).toDTO());
  }

  public ResponseEntity<Void> deleteDashboard(DashboardDTO dashboardDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!(authentication.getPrincipal() instanceof DashUserDetails userDetails)) {
      return ResponseEntity.status(401).build();
    }

    UUID dashboardUuid = UUID.fromString(dashboardDTO.getUuid());
    Optional<DashboardEntity> dashboardEntity = dashboardRepository.findById(dashboardUuid);

    if (dashboardEntity.isEmpty() || !dashboardEntity.get().getOwner().getId().equals(userDetails.getId())) {
      return ResponseEntity.status(401).build();
    }

    dashboardRepository.delete(dashboardEntity.get());
    return ResponseEntity.ok().build();
  }
}