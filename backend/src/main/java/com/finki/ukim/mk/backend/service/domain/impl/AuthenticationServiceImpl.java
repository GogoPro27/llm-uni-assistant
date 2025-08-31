package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.database.model.User;
import com.finki.ukim.mk.backend.database.repository.UserRepository;
import com.finki.ukim.mk.backend.dto.LoginRequest;
import com.finki.ukim.mk.backend.dto.LoginResponse;
import com.finki.ukim.mk.backend.security.CustomUserDetailsService;
import com.finki.ukim.mk.backend.security.JwtService;
import com.finki.ukim.mk.backend.service.domain.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailsService customUserDetailsService;

  public LoginResponse login(LoginRequest request) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
      )
    );

    User user = userRepository.findByEmail(request.getEmail())
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
    String jwtToken = jwtService.generateToken(userDetails);

    return LoginResponse.builder()
      .token(jwtToken)
      .email(user.getEmail())
      .name(user.getName())
      .surname(user.getSurname())
      .roles(user.getRoles())
      .userId(user.getId())
      .build();
  }

  @Override
  public User getCurrentUser() {
    User user = getCurrentUserOrNull();
    if (user == null) {
      throw new RuntimeException("No authenticated user found");
    }
    return user;
  }

  @Override
  public boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null
      && authentication.isAuthenticated()
      && !"anonymousUser".equals(authentication.getPrincipal());
  }

  private User getCurrentUserOrNull() {
    String email = getCurrentUserEmail();
    if (email == null) {
      return null;
    }

    return userRepository.findByEmail(email)
      .orElse(null);
  }

  private String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof UserDetails) {
      return ((UserDetails) principal).getUsername();
    } else if (principal instanceof String) {
      return (String) principal;
    }

    return null;
  }
}
