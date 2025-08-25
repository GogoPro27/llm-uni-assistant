package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.database.model.ChatMessage;
import com.finki.ukim.mk.backend.database.repository.ChatMessageRepository;
import com.finki.ukim.mk.backend.service.domain.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
  private final ChatMessageRepository chatMessageRepository;

  @Override
  public ChatMessage save(ChatMessage message) {
    return chatMessageRepository.save(message);
  }
}