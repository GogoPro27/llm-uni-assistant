package com.finki.ukim.mk.backend.service.domain;

import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.database.model.Subject;

import java.util.List;

public interface ProfessorGroupSubjectService {
  ProfessorGroupSubject findById(Long id);
  void save(ProfessorGroupSubject group);
  void deleteById(Long id);
  Boolean existsBySubjectAndMembersContains(Subject subject);
  ProfessorGroupSubject changeGroupForSubject(Long subjectId, Long newGroupId);
  List<Subject> getEnrolledSubjects();
  List<ProfessorGroupSubject> getAllGroupsBySubjectId(Long subjectId);
}
