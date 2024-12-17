package com.xyz.backend.authentication.dashboard;

import com.xyz.backend.authentication.user.DashUserDetails;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface DashboardRepository extends CrudRepository<DashboardEntity, UUID> {
  List<DashboardEntity> findAllByOwner(DashUserDetails owner);
}
