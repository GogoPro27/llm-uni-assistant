package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.ChatMessage;
import com.finki.ukim.mk.backend.database.model.ChatSession;
import com.finki.ukim.mk.backend.database.model.enums.MessageOrigin;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ChatMessageRequestDto {
  @NotNull
  String message;
  @NotNull
  Long chatSessionId;

  public ChatMessage toChatMessage(ChatSession chatSession) {
    return ChatMessage.builder()
      .content(message)
      .session(chatSession)
      .origin(MessageOrigin.user)
      .build();
  }
}
