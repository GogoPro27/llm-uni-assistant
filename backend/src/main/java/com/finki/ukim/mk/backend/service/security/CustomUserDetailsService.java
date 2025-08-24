package com.finki.ukim.mk.backend.service.security;

import com.finki.ukim.mk.backend.database.model.User;
import com.finki.ukim.mk.backend.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    return org.springframework.security.core.userdetails.User.builder()
      .username(user.getEmail())
      .password(user.getPasswordHash())
      .authorities(user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name().toUpperCase()))
        .collect(Collectors.toList()))
      .build();
  }
}
