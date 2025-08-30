package com.finki.ukim.mk.backend.service.domain;

import com.finki.ukim.mk.backend.database.model.ChatMessage;
import com.finki.ukim.mk.backend.database.model.ChatSession;

public interface ChatSessionService {
  ChatSession openSession(Long subjectId);
  ChatSession getSessionWithMessagesById(Long sessionId);
  ChatMessage sendMessageInSession(ChatMessage message);
  void deleteSession(Long sessionId);
  ChatSession getSessionById(Long sessionId);
}