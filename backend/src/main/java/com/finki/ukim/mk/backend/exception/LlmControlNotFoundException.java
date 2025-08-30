package com.finki.ukim.mk.backend.exception;

public class LlmControlNotFoundException extends RuntimeException {
  public LlmControlNotFoundException(Long llmControlId) {
    super("LLM control not found with id: " + llmControlId);
  }
}
