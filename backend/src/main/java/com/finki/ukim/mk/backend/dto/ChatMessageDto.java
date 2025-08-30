package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ChatMessageDto {
  private String message;
  private Long sessionId;
  private OffsetDateTime timeStamp;

  public static ChatMessageDto fromChatMessage(ChatMessage message) {
    return new ChatMessageDto(message.getContent(), message.getSession().getId(), message.getCreatedAt());
  }
}
