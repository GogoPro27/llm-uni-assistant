package com.finki.ukim.mk.backend.service.application;

import com.finki.ukim.mk.backend.database.model.ChatSession;
import com.finki.ukim.mk.backend.dto.ChatMessageDto;
import com.finki.ukim.mk.backend.dto.ChatMessageRequestDto;
import com.finki.ukim.mk.backend.dto.ChatSessionLightDto;
import com.finki.ukim.mk.backend.dto.ChatSessionWithMessagesDto;

import java.util.List;

public interface ChatSessionApplicationService {
  ChatSessionWithMessagesDto openSession(Long subjectId);
  ChatSessionWithMessagesDto getSessionWithMessagesById(Long sessionId);
  ChatMessageDto sendMessageInSession(ChatMessageRequestDto message);
  void deleteSession(Long sessionId);
  List<ChatSessionLightDto> getSessionsBySubjectId(Long subjectId);
}