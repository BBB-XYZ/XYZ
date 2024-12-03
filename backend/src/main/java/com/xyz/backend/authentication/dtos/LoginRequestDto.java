package com.xyz.backend.authentication.dtos;

public record LoginRequestDto(String username, String password) {

  @Override
  public String username() {
    return username;
  }

  @Override
  public String password() {
    return password;
  }
}