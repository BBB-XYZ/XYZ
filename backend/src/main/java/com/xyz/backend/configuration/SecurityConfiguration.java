package com.xyz.backend.configuration;

import com.xyz.backend.authentication.SessionAuthenticationFilter;
import com.xyz.backend.authentication.user.DashUserDetailsService;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

  private DashUserDetailsService userDetailsManager;
  private SessionAuthenticationFilter sessionAuthenticationFilter;

  @Bean
  public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .addFilterBefore(sessionAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .securityContext(customizer ->
            customizer.securityContextRepository(securityContextRepository()))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(request -> {
          CorsConfiguration configuration = new CorsConfiguration();
          configuration.setAllowedOrigins(Arrays.asList("*"));
          configuration.setAllowedMethods(Arrays.asList("*"));
          configuration.setAllowedHeaders(Arrays.asList("*"));
          return configuration;
        }));
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