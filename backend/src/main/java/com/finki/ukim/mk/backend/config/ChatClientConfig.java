package com.finki.ukim.mk.backend.config;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
  @Value("${rag.simillarity-threshold}")
  private Double similarityThreshold;
  @Value("${rag.top-k}")
  private Integer topK;

  @Bean("openai")
  public ChatClient openAiChatClient(OpenAiChatModel chatModel, RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
    return ChatClient.builder(chatModel)
      .defaultAdvisors(retrievalAugmentationAdvisor, new SimpleLoggerAdvisor())
      .build();
  }

  @Bean("anthropic")
  public ChatClient anthropicChatClient(AnthropicChatModel chatModel, RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
    return ChatClient.builder(chatModel)
      .defaultAdvisors(retrievalAugmentationAdvisor, new SimpleLoggerAdvisor())
      .build();
  }

  @Bean
  RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore) {
    VectorStoreDocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
      .similarityThreshold(similarityThreshold)
      .topK(topK)
      .vectorStore(vectorStore)
      .build();

    return RetrievalAugmentationAdvisor.builder()
      .documentRetriever(documentRetriever)
      .build();
  }
}