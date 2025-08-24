package com.finki.ukim.mk.backend.service.security;

import com.finki.ukim.mk.backend.database.model.User;
import com.finki.ukim.mk.backend.database.repository.UserRepository;
import com.finki.ukim.mk.backend.dto.LoginRequest;
import com.finki.ukim.mk.backend.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

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
}
