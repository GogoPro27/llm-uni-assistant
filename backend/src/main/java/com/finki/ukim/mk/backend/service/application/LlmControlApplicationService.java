package com.finki.ukim.mk.backend.service.application;

import com.finki.ukim.mk.backend.dto.CreateLlmControlDto;
import com.finki.ukim.mk.backend.dto.LlmControlDto;

public interface LlmControlApplicationService {
  LlmControlDto getLlmControlByGroupSubject(Long professorGroupSubjectId);
  LlmControlDto saveLlmControl(CreateLlmControlDto newLlmControl);
  void deleteLlmControlById(Long llmControlId);
  LlmControlDto updateLlmControl(LlmControlDto llmControl);
}
