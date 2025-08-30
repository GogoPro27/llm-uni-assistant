package com.finki.ukim.mk.backend.service.application;

import com.finki.ukim.mk.backend.dto.ChatMessageDto;
import com.finki.ukim.mk.backend.dto.ChatMessageRequestDto;
import com.finki.ukim.mk.backend.dto.ChatSessionWithMessagesDto;

public interface ChatSessionApplicationService {
  ChatSessionWithMessagesDto openSession(Long subjectId);
  ChatSessionWithMessagesDto getSessionWithMessagesById(Long sessionId);
  ChatMessageDto sendMessageInSession(ChatMessageRequestDto message);
  void deleteSession(Long sessionId);
}