package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.database.model.ChatMessage;
import com.finki.ukim.mk.backend.database.model.ChatSession;
import com.finki.ukim.mk.backend.database.model.LlmControl;
import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.database.repository.ChatMessageRepository;
import com.finki.ukim.mk.backend.factory.RagAdvisorFactory;
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
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LlmServiceImpl implements LlmService {
  private final ChatMessageRepository chatMessageRepository;
  private final ChatMessageMapper chatMessageMapper;
  private final Map<String, ChatClient> chatClients;
  private final RagAdvisorFactory advisorFactory;

  @Override
  public ChatMessage sendMessage(ChatMessage message) {

    ChatSession session = message.getSession();
    ProfessorGroupSubject groupSubject = message.getSession().getEnrollment().getGroupSubject();
    LlmControl llmControl = groupSubject.getLlmControl();
    assert llmControl != null;

    List<ChatMessage> messages = chatMessageRepository.findBySessionOrderByCreatedAtAsc(session);
    List<ChatMessage> lastMessages = truncateMessagesToMemorySize(messages, llmControl.getMemoryWindowSize());

    List<Message> messagesToSend = lastMessages.stream().map(chatMessageMapper::to).collect(Collectors.toList());

    if (!llmControl.getInstructions().isBlank()) {
      SystemMessage systemMessage = new SystemMessage(llmControl.getInstructions());
      messagesToSend.addFirst(systemMessage);
    }

    ChatOptions options = createOptionsAtRuntime(llmControl);
    Prompt prompt = new Prompt(messagesToSend, options);

    boolean allowEmptyContext = !llmControl.isStrictRag();
    RetrievalAugmentationAdvisor ragAdvisor = advisorFactory.build(llmControl.getTopK(), llmControl.getSimilarityThreshold(), allowEmptyContext, llmControl.isRelaxedAnswers());

    Filter.Expression groupIdFilter = new FilterExpressionBuilder()
      .eq("group_id", String.valueOf(groupSubject.getId()))
      .build();

    ChatResponse chatResponse = chatClients.get(llmControl.getLlmProvider())
      .prompt(prompt)
      .advisors(ragAdvisor)
      .advisors(a -> a.param(VectorStoreDocumentRetriever.FILTER_EXPRESSION, groupIdFilter))
      .call()
      .chatResponse();

    assert chatResponse != null;
    return chatMessageMapper.from(chatResponse.getResult().getOutput(), session);
  }

  private List<ChatMessage> truncateMessagesToMemorySize(List<ChatMessage> messages, Integer memorySize) {
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
