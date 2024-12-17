package com.xyz.backend.dtos;

public record GenericMessageDTO(String message) {
  public String message() {
    return message;
  }
}