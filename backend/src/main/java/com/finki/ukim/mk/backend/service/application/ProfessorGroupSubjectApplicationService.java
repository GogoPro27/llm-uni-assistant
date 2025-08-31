package com.finki.ukim.mk.backend.service.application;

import com.finki.ukim.mk.backend.dto.ProfessorGroupSubjectDto;
import com.finki.ukim.mk.backend.dto.SubjectLightDto;

import java.util.List;

public interface ProfessorGroupSubjectApplicationService {
  ProfessorGroupSubjectDto findById(Long id);
  void deleteById(Long id);
  ProfessorGroupSubjectDto changeGroupForSubject(Long subjectId, Long newGroupId);
  List<SubjectLightDto> getEnrolledSubjects();
}
