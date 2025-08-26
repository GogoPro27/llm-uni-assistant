package com.finki.ukim.mk.backend.exception;

public class ProfessorAlreadyInGroupException extends RuntimeException {
  public ProfessorAlreadyInGroupException(String message) {
    super(message);
  }

  public ProfessorAlreadyInGroupException(Long userId, Long subjectId) {
    super("Professor with ID: " + userId + " is already in group with ID: " + subjectId);
  }
}