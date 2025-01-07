package com.xyz.backend.dashboard.dtos;

import com.xyz.backend.dashboard.widget.dtos.WidgetDTO;
import com.xyz.backend.authentication.user.dtos.DashUserDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardDTO {
  private String uuid;
  private String name;
  private DashUserDetailsDTO owner;

  private WidgetDTO[] widgets;
}
