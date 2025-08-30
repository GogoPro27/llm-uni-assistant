package com.finki.ukim.mk.backend.service.application.impl;

import com.finki.ukim.mk.backend.database.model.LlmControl;
import com.finki.ukim.mk.backend.dto.CreateLlmControlDto;
import com.finki.ukim.mk.backend.dto.LlmControlDto;
import com.finki.ukim.mk.backend.service.application.LlmControlApplicationService;
import com.finki.ukim.mk.backend.service.domain.LlmControlService;
import com.finki.ukim.mk.backend.util.LlmParametersUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LlmControlApplicationServiceImpl implements LlmControlApplicationService {

  private final LlmControlService llmControlService;

  @Override
  public LlmControlDto getLlmControlByGroupSubject(Long professorGroupSubjectId) {
    LlmControl llmControl = llmControlService.getLlmControlByGroupSubject(professorGroupSubjectId);
    return LlmControlDto.fromLlmControl(llmControl);
  }

  @Override
  public LlmControlDto saveLlmControl(CreateLlmControlDto newLlmControl) {
    LlmControl llmControl = llmControlService.saveLlmControl(newLlmControl.toLlmControl());
    return LlmControlDto.fromLlmControl(llmControl);
  }

  @Override
  public void deleteLlmControlById(Long llmControlId) {
    llmControlService.deleteLlmControlById(llmControlId);
  }

  @Override
  public LlmControlDto updateLlmControl(LlmControlDto llmControl) {
    LlmControl existingLlmControl = llmControlService.getLlmControlById(llmControl.getId());
    existingLlmControl.setLlmProvider(llmControl.getLlmProvider());
    existingLlmControl.setInstructions(llmControl.getSystemPrompt());
    existingLlmControl.setModelName(llmControl.getModelName());
    existingLlmControl.setParams(LlmParametersUtils.serialize(llmControl.getParams()));

    LlmControl updatedLlmControl = llmControlService.saveLlmControl(existingLlmControl);
    return LlmControlDto.fromLlmControl(updatedLlmControl);
  }
}
