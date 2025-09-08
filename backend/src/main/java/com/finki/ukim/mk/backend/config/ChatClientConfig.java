package com.finki.ukim.mk.backend.config;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
  @Bean("openai")
  public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
    return ChatClient.builder(chatModel)
      .defaultAdvisors(new SimpleLoggerAdvisor())
      .build();
  }

  @Bean("anthropic")
  public ChatClient anthropicChatClient(AnthropicChatModel chatModel) {
    return ChatClient.builder(chatModel)
      .defaultAdvisors(new SimpleLoggerAdvisor())
      .build();
  }

}