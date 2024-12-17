package com.xyz.backend.authentication.dashboard.widget;

import com.xyz.backend.authentication.dashboard.DashboardEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetRepository extends CrudRepository<WidgetEntity, Long> {
  void deleteAllByDashboard(DashboardEntity dashboard);
}
