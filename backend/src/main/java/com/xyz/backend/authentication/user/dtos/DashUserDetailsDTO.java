package com.xyz.backend.authentication.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DashUserDetailsDTO {
  private long id;
  private String username;
  private String[] grantedAuthorities;
}
