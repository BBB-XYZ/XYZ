package com.xyz.backend.authentication.dashboard;

import com.xyz.backend.authentication.dashboard.dtos.DashboardDTO;
import com.xyz.backend.authentication.user.DashUserDetails;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DashboardEntity {
  private UUID uuid;
  private String name;
  private DashUserDetails owner;

  public DashboardDTO toDTO() {
    return new DashboardDTO(uuid.toString(), name, owner.toDTO());
  }
}
