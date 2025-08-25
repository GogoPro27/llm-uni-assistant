package com.finki.ukim.mk.backend.service.domain;

import com.finki.ukim.mk.backend.database.model.ChatMessage;

public interface LlmService {
  ChatMessage sendMessage(ChatMessage message);
}
