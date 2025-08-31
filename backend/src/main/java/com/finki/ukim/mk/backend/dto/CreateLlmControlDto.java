package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.LlmControl;
import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.util.LlmParametersUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLlmControlDto {
  @NotNull
  private Long professorGroupSubjectId;
  private String modelName;
  private String llmProvider;
  private String systemPrompt;
  private Map<String, Object> params;

  public LlmControl toLlmControl(ProfessorGroupSubject professorGroupSubject) {
    return LlmControl.builder()
      .groupSubject(professorGroupSubject)
      .modelName(modelName)
      .llmProvider(llmProvider)
      .instructions(systemPrompt)
      .params(params != null ? LlmParametersUtils.serialize(params) : "{}")
      .build();
  }
}
