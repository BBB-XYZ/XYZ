package com.xyz.backend.dashboard.widget;

import com.xyz.backend.dashboard.DashboardEntity;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetRepository extends CrudRepository<WidgetEntity, UUID> {
  void deleteAllByDashboard(DashboardEntity dashboard);
}
