package com.finki.ukim.mk.backend.service.domain;

import com.finki.ukim.mk.backend.database.model.User;
import com.finki.ukim.mk.backend.dto.LoginRequest;
import com.finki.ukim.mk.backend.dto.LoginResponse;

public interface AuthenticationService {
  User getCurrentUser();
  boolean isAuthenticated();
  LoginResponse login(LoginRequest request);
}