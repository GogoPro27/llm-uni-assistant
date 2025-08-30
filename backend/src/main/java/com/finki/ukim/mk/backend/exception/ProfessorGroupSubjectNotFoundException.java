package com.finki.ukim.mk.backend.exception;

public class ProfessorGroupSubjectNotFoundException extends RuntimeException {
  public ProfessorGroupSubjectNotFoundException(Long id) {
    super("Professor group not found with id: " + id);
  }
  public ProfessorGroupSubjectNotFoundException(Long subjectId, Long professorId) {
    super("Professor group not found for subject with id: " + subjectId + " and professor with id: " + professorId);
  }
}