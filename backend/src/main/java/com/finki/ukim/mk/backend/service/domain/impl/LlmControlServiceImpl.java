package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.database.model.LlmControl;
import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.database.repository.LlmControlRepository;
import com.finki.ukim.mk.backend.exception.LlmControlNotFoundException;
import com.finki.ukim.mk.backend.service.domain.LlmControlService;
import com.finki.ukim.mk.backend.service.domain.ProfessorGroupSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LlmControlServiceImpl implements LlmControlService {
  private final LlmControlRepository llmControlRepository;
  private final ProfessorGroupSubjectService professorGroupSubjectService;

  @Override
  public LlmControl getLlmControlById(Long llmControlId) {
    return llmControlRepository.findById(llmControlId).orElseThrow(() -> new LlmControlNotFoundException(llmControlId));
  }

  @Override
  public LlmControl getLlmControlByGroupSubject(Long professorGroupSubjectId) {
    ProfessorGroupSubject professorGroupSubject = professorGroupSubjectService.findById(professorGroupSubjectId);
    return professorGroupSubject.getLlmControl();
  }

  @Override
  @Transactional
  public LlmControl saveLlmControl(LlmControl llmControl) {
    return llmControlRepository.save(llmControl);
  }

  @Override
  @Transactional
  public void deleteLlmControlById(Long llmControlId) {
    if (!llmControlRepository.existsById(llmControlId)) {
      throw new LlmControlNotFoundException(llmControlId);
    }
    llmControlRepository.deleteById(llmControlId);
  }
}
