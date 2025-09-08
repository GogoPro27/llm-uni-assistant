package com.finki.ukim.mk.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "llm.default")
public class LlmDefaultsProperties {
  private String modelName;
  private String llmProvider;
  private String systemPrompt;
  private Integer memorySize;
  private double ragSimilarityThreshold;
  private int ragTopK;
  private boolean ragStrict;
  private boolean ragRelaxedAnswers;
}