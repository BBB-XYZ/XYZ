package com.xyz.backend;

import com.xyz.backend.authentication.AuthenticationService;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@NoArgsConstructor
public class TestDataService {
  private AuthenticationService authenticationService;
}