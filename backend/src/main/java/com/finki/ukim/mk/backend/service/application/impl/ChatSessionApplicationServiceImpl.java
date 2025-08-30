package com.finki.ukim.mk.backend.service.application.impl;

import com.finki.ukim.mk.backend.database.model.ChatMessage;
import com.finki.ukim.mk.backend.database.model.ChatSession;
import com.finki.ukim.mk.backend.dto.ChatMessageDto;
import com.finki.ukim.mk.backend.dto.ChatMessageRequestDto;
import com.finki.ukim.mk.backend.dto.ChatSessionWithMessagesDto;
import com.finki.ukim.mk.backend.service.application.ChatSessionApplicationService;
import com.finki.ukim.mk.backend.service.domain.ChatSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatSessionApplicationServiceImpl implements ChatSessionApplicationService {
  private final ChatSessionService chatSessionService;

  @Override
  public ChatSessionWithMessagesDto openSession(Long subjectId) {
    ChatSession chatSession = chatSessionService.openSession(subjectId);

    return ChatSessionWithMessagesDto.fromChatSession(chatSession);
  }

  @Override
  public ChatSessionWithMessagesDto getSessionWithMessagesById(Long sessionId) {
    ChatSession chatSession = chatSessionService.getSessionWithMessagesById(sessionId);

    return ChatSessionWithMessagesDto.fromChatSession(chatSession);
  }

  @Override
  public ChatMessageDto sendMessageInSession(ChatMessageRequestDto message) {
    ChatSession chatSession = chatSessionService.getSessionById(message.getChatSessionId());

    ChatMessage chatMessage = message.toChatMessage(chatSession);
    ChatMessage receivedMessage = chatSessionService.sendMessageInSession(chatMessage);

    return ChatMessageDto.fromChatMessage(receivedMessage);
  }

  @Override
  public void deleteSession(Long sessionId) {
    chatSessionService.deleteSession(sessionId);
  }

}
