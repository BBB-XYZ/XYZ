package com.xyz.backend.configuration;

import com.xyz.backend.authentication.SessionAuthenticationFilter;
import com.xyz.backend.authentication.user.DashUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfiguration {
  private DashUserDetailsService userDetailsManager;
  private SessionAuthenticationFilter sessionAuthenticationFilter;

  @Bean
  public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(requestAttributes ->
        requestAttributes.requestMatchers("/api/login", "/api/register", "/api/test").permitAll()
            .anyRequest().authenticated()).addFilterBefore(sessionAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).securityContext(customizer ->
        customizer.securityContextRepository(securityContextRepository()));
    return http.build();
  }

  @Bean
  public HttpSessionSecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}