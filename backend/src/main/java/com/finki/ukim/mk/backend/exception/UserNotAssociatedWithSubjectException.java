package com.finki.ukim.mk.backend.exception;

public class UserNotAssociatedWithSubjectException extends RuntimeException {
  public UserNotAssociatedWithSubjectException(Long id) {
    super("User is not associated with subject ID: " + id);
  }
}
