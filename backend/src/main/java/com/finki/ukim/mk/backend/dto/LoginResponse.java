package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

  private String token;
  private String email;
  private String name;
  private String surname;
  private Set<UserRole> roles;
  private Long userId;
}
