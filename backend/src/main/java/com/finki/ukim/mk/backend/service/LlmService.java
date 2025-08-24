package com.finki.ukim.mk.backend.service;

import com.finki.ukim.mk.backend.database.model.ChatMessage;

public interface LlmService {
  ChatMessage sendMessage(ChatMessage message);
}
