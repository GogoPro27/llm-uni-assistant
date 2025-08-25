package com.finki.ukim.mk.backend.exception;

public class SubjectNotFoundException extends RuntimeException {
  public SubjectNotFoundException(Long id) {
    super("Subject not found with id: " + id);
  }
}
