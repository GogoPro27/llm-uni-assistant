package com.finki.ukim.mk.backend.exception;

public class UserNotEnrolledException extends RuntimeException {
  public UserNotEnrolledException(Long userId, Long subjectId) {
    super("User with ID: " + userId + " is not enrolled in subject with ID: " + subjectId);
  }
}