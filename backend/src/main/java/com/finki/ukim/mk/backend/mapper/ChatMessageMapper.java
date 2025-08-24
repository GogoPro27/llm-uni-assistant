package com.finki.ukim.mk.backend.mapper;

import com.finki.ukim.mk.backend.database.model.ChatMessage;
import com.finki.ukim.mk.backend.database.model.ChatSession;
import com.finki.ukim.mk.backend.database.model.enums.MessageOrigin;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {
  public Message to(ChatMessage chatMessage) {
    Message message;
    if (chatMessage.getOrigin() == MessageOrigin.user) {
      message = new UserMessage(chatMessage.getContent());
    } else {
      message = new AssistantMessage(chatMessage.getContent());
    }

    return message;
  }

  public ChatMessage from(Message message, ChatSession chatSession) {
    ChatMessage chatMessage = new ChatMessage();

    if (message instanceof UserMessage) {
      chatMessage.setContent(((UserMessage) message).getText());
      chatMessage.setOrigin(MessageOrigin.user);
    } else if (message instanceof AssistantMessage) {
      chatMessage.setContent(((AssistantMessage) message).getText());
      chatMessage.setOrigin(MessageOrigin.assistant);
    } else throw new IllegalArgumentException("Unknown message type");

    chatMessage.setSession(chatSession);
    return chatMessage;
  }
}
