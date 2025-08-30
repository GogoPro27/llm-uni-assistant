package com.finki.ukim.mk.backend.service.application;

import com.finki.ukim.mk.backend.dto.ProfessorGroupSubjectDto;

public interface ProfessorGroupSubjectApplicationService {
  ProfessorGroupSubjectDto findById(Long id);
  void deleteById(Long id);
  ProfessorGroupSubjectDto changeGroupForSubject(Long subjectId, Long newGroupId);
}
