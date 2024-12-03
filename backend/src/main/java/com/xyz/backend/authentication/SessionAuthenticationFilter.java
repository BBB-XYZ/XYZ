package com.xyz.backend.authentication;

import com.xyz.backend.authentication.user.DashUserDetails;
import com.xyz.backend.authentication.user.DashUserDetailsRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {
  private DashUserDetailsRepository userDetailsRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = request.getHeader("Authorization");

    if (token == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    Optional<DashUserDetails> userDetails = userDetailsRepository.findBySession_Token(token);
    if (userDetails.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails.get(), token,
        userDetails.get().getAuthorities()));

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.startsWith("/api/login") || path.startsWith("/api/register");
  }
}