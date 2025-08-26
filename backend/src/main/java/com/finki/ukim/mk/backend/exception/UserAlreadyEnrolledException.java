package com.finki.ukim.mk.backend.exception;

public class UserAlreadyEnrolledException extends RuntimeException {
  public UserAlreadyEnrolledException(String message) {
    super(message);
  }

  public UserAlreadyEnrolledException(Long userId, Long subjectId) {
    super("User with ID: " + userId + " is already enrolled in subject with ID: " + subjectId);
  }
}