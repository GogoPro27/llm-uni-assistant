package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.LlmControl;
import com.finki.ukim.mk.backend.util.LlmParametersUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CreateLlmControlDto {
  private String modelName;
  private String llmProvider;
  private String systemPrompt;
  private Map<String, Object> params;

  public LlmControl toLlmControl() {
    return LlmControl.builder()
      .modelName(modelName)
      .llmProvider(llmProvider)
      .instructions(systemPrompt)
      .params(LlmParametersUtils.serialize(params))
      .build();
  }
}
