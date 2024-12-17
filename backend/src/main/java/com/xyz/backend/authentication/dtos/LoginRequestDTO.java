package com.xyz.backend.authentication.dtos;

public record LoginRequestDTO(String username, String password) {

  @Override
  public String username() {
    return username;
  }

  @Override
  public String password() {
    return password;
  }
}