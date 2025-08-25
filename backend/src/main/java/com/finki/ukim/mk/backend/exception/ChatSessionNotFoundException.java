package com.finki.ukim.mk.backend.exception;

public class ChatSessionNotFoundException extends RuntimeException {
  public ChatSessionNotFoundException(Long id) {
    super("Chat session not found with ID: " + id);
  }
}