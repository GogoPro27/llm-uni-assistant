package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.database.model.ChatMessage;
import com.finki.ukim.mk.backend.database.model.ChatSession;
import com.finki.ukim.mk.backend.database.model.LlmControl;
import com.finki.ukim.mk.backend.database.repository.ChatMessageRepository;
import com.finki.ukim.mk.backend.mapper.ChatMessageMapper;
import com.finki.ukim.mk.backend.service.domain.LlmService;
import com.finki.ukim.mk.backend.util.LlmParametersUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LlmServiceImpl implements LlmService {
  @Value("${llm.system-prompt}")
  private String systemPrompt;

  @Value("${llm.memory-size}")
  private Integer memorySize;

  private final ChatMessageRepository chatMessageRepository;
  private final ChatMessageMapper chatMessageMapper;
  private final Map<String, ChatClient> chatClients;

  @Override
  public ChatMessage sendMessage(ChatMessage message) {

    ChatSession session = message.getSession();
    List<ChatMessage> messages = chatMessageRepository.findBySessionOrderByCreatedAtAsc(session);
    List<ChatMessage> lastMessages = truncateMessagesToMemorySize(messages);

    List<Message> messagesToSend = lastMessages.stream().map(chatMessageMapper::to).toList();
    SystemMessage systemMessage = new SystemMessage(systemPrompt);
    messagesToSend.addFirst(systemMessage);

    LlmControl llmControl = message.getSession().getEnrollment().getGroupSubject().getLlmControl();
    ChatOptions options = createOptionsAtRuntime(llmControl);

    Prompt prompt = new Prompt(messagesToSend, options);

    String provider = llmControl != null && llmControl.getLlmProvider() != null ? llmControl.getLlmProvider() : "openai";
    ChatResponse chatResponse = chatClients.get(provider)
      .prompt(prompt)
      .call()
      .chatResponse();

    assert chatResponse != null;
    return chatMessageMapper.from(chatResponse.getResult().getOutput(), session);
  }

  private List<ChatMessage> truncateMessagesToMemorySize(List<ChatMessage> messages) {
    return messages.size() <= memorySize ? messages : messages.subList(messages.size() - memorySize, messages.size());
  }

  private ChatOptions createOptionsAtRuntime(LlmControl llmControl) {
    ChatOptions.Builder builder = ChatOptions.builder();

    if (llmControl != null) {
      if (llmControl.getModelName() != null) {
        builder.model(llmControl.getModelName());
      }

      if (llmControl.getParams() != null) {
        Map<String, Object> paramsMap = LlmParametersUtils.deserialize(llmControl.getParams());

        LlmParametersUtils.getDoubleParameter(paramsMap, "temperature")
          .ifPresent(builder::temperature);

        LlmParametersUtils.getIntParameter(paramsMap, "maxTokens")
          .ifPresent(builder::maxTokens);
      }
    }

    return builder.build();
  }
}
