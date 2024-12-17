package com.xyz.backend.authentication.dashboard;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface DashboardRepository extends CrudRepository<DashboardEntity, UUID> {

}
