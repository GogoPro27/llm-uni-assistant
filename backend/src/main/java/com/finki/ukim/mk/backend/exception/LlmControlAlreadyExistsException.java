package com.finki.ukim.mk.backend.exception;

public class LlmControlAlreadyExistsException extends RuntimeException {
  public LlmControlAlreadyExistsException(Long llmControlId) {
    super("LLM control already exists with id: " + llmControlId);
  }
}
