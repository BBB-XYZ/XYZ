package com.xyz.backend.dtos;

public record GenericMessageDto(String message) {
  public String message() {
    return message;
  }
}