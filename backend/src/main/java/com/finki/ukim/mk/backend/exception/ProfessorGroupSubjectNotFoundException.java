package com.finki.ukim.mk.backend.exception;

public class ProfessorGroupSubjectNotFoundException extends RuntimeException {
  public ProfessorGroupSubjectNotFoundException(String message) {
    super(message);
  }
  public ProfessorGroupSubjectNotFoundException(Long id) {
    super("Professor group not found with id: " + id);
  }
}