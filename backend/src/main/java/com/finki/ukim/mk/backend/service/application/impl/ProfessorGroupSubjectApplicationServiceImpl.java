package com.finki.ukim.mk.backend.service.application.impl;

import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.dto.ProfessorGroupSubjectDto;
import com.finki.ukim.mk.backend.dto.light.ProfessorGroupSubjectLightDto;
import com.finki.ukim.mk.backend.dto.light.SubjectLightDto;
import com.finki.ukim.mk.backend.service.application.ProfessorGroupSubjectApplicationService;
import com.finki.ukim.mk.backend.service.domain.ProfessorGroupSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorGroupSubjectApplicationServiceImpl implements ProfessorGroupSubjectApplicationService {

  private final ProfessorGroupSubjectService professorGroupSubjectService;


  @Override
  public ProfessorGroupSubjectDto findById(Long id) {
    ProfessorGroupSubject groupSubject = professorGroupSubjectService.findById(id);
    return ProfessorGroupSubjectDto.fromProfessorGroupSubject(groupSubject);
  }

  @Override
  public void deleteById(Long id) {
    professorGroupSubjectService.deleteById(id);
  }

  @Override
  public ProfessorGroupSubjectDto changeGroupForSubject(Long subjectId, Long newGroupId) {
    ProfessorGroupSubject professorGroupSubject = professorGroupSubjectService.changeGroupForSubject(subjectId, newGroupId);
    return ProfessorGroupSubjectDto.fromProfessorGroupSubject(professorGroupSubject);
  }

  @Override
  public List<SubjectLightDto> getEnrolledSubjects() {
    return professorGroupSubjectService.getEnrolledSubjects().stream()
      .map(SubjectLightDto::fromSubject)
      .toList();
  }

  @Override
  public List<ProfessorGroupSubjectLightDto> getAllGroupsBySubjectId(Long subjectId) {
    return professorGroupSubjectService.getAllGroupsBySubjectId(subjectId).stream()
      .map(ProfessorGroupSubjectLightDto::fromProfessorGroupSubject)
      .toList();
  }
}
