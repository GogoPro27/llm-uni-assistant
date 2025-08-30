package com.finki.ukim.mk.backend.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finki.ukim.mk.backend.database.model.LlmControl;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class LlmControlDto {
  private Long id;
  private String modelName;
  private String llmProvider;
  private String systemPrompt;
  private Map<String, Object> params;

  public static LlmControlDto fromLlmControl(LlmControl llmControl) {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> paramsMap;

    try {
      paramsMap = mapper.readValue(llmControl.getParams(),
        new TypeReference<>() {
        });
    } catch (Exception e) {
      paramsMap = new HashMap<>();
    }

    return new LlmControlDto(
      llmControl.getId(),
      llmControl.getModelName(),
      llmControl.getLlmProvider(),
      llmControl.getInstructions(),
      paramsMap
    );
  }
}
