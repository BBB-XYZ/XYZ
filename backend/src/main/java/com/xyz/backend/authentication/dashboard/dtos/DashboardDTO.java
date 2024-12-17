package com.xyz.backend.authentication.dashboard.dtos;

import com.fasterxml.jackson.annotation.JsonAnySetter;
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
}
