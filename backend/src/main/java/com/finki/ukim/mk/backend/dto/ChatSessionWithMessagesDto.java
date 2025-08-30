package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.ChatSession;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ChatSessionWithMessagesDto {
  private Long sessionId;
  private List<ChatMessageDto> messages;
  private OffsetDateTime updatedAt;

  public static ChatSessionWithMessagesDto fromChatSession(ChatSession chatSession) {
    return new ChatSessionWithMessagesDto(
      chatSession.getId(),
      chatSession.getMessages().stream().map(ChatMessageDto::fromChatMessage).toList(),
      chatSession.getUpdatedAt()
    );
  }
}
