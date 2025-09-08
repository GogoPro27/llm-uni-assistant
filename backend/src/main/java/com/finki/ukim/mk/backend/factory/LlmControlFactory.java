package com.finki.ukim.mk.backend.factory;

import com.finki.ukim.mk.backend.config.LlmDefaultsProperties;
import com.finki.ukim.mk.backend.database.model.LlmControl;
import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class LlmControlFactory {

  private final LlmDefaultsProperties defaults;

  public LlmControl create(ProfessorGroupSubject subject, Consumer<LlmControl.LlmControlBuilder> overrides) {
    LlmControl.LlmControlBuilder builder = LlmControl.builder()
      .groupSubject(subject)
      .modelName(defaults.getModelName())
      .llmProvider(defaults.getLlmProvider())
      .instructions(defaults.getSystemPrompt())
      .strictRag(defaults.isRagStrict())
      .relaxedAnswers(defaults.isRagRelaxedAnswers())
      .topK(defaults.getRagTopK())
      .similarityThreshold(defaults.getRagSimilarityThreshold())
      .memoryWindowSize(defaults.getMemorySize());

    if (overrides != null) {
      overrides.accept(builder);
    }

    return builder.build();
  }

  public LlmControl create(ProfessorGroupSubject subject) {
    return create(subject, null);
  }
}