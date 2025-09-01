package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.ChatSession;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ChatSessionLightDto {
  private Long id;
  private String title;
  private OffsetDateTime updatedAt;

  public static ChatSessionLightDto fromChatSession(ChatSession chatSession) {
    return new ChatSessionLightDto(
      chatSession.getId(),
      chatSession.getTitle(),
      chatSession.getUpdatedAt());
  }
}
