package com.xyz.backend.authentication.session;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "user_sessions")
@Getter
@Setter
public class UserSessionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String token;

  @Column(nullable = false)
  private long expiresAt;
}