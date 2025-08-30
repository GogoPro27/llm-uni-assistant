package com.finki.ukim.mk.backend.service.domain;

import com.finki.ukim.mk.backend.database.model.LlmControl;

public interface LlmControlService {
  LlmControl getLlmControlById(Long llmControlId);
  LlmControl getLlmControlByGroupSubject(Long professorGroupSubjectId);
  LlmControl saveLlmControl(LlmControl llmControl);
  void deleteLlmControlById(Long llmControlId);
}
