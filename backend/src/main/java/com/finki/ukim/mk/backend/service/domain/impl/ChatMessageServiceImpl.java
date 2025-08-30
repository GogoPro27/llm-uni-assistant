package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.database.model.ChatMessage;
import com.finki.ukim.mk.backend.database.repository.ChatMessageRepository;
import com.finki.ukim.mk.backend.service.domain.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageServiceImpl implements ChatMessageService {
  private final ChatMessageRepository chatMessageRepository;

  @Override
  @Transactional
  public ChatMessage save(ChatMessage message) {
    return chatMessageRepository.save(message);
  }

  @Override
  public List<ChatMessage> getChatMessagesBySessionId(Long sessionId) {
    return chatMessageRepository.findBySession_IdOrderByCreatedAtAsc(sessionId);
  }

  @Override
  @Transactional
  public void deleteAllBySessionId(Long sessionId) {
    chatMessageRepository.deleteBySession_Id(sessionId);
  }
}