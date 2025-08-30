package com.finki.ukim.mk.backend.service.domain;

import com.finki.ukim.mk.backend.database.model.ChatMessage;

import java.util.List;

public interface ChatMessageService {
  ChatMessage save(ChatMessage message);
  List<ChatMessage> getChatMessagesBySessionId(Long sessionId);
  void deleteAllBySessionId(Long sessionId);
}