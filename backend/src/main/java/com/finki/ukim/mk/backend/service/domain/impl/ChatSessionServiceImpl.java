package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.database.model.ChatMessage;
import com.finki.ukim.mk.backend.database.model.ChatSession;
import com.finki.ukim.mk.backend.database.model.Enrollment;
import com.finki.ukim.mk.backend.database.model.User;
import com.finki.ukim.mk.backend.database.repository.ChatSessionRepository;
import com.finki.ukim.mk.backend.database.repository.EnrollmentRepository;
import com.finki.ukim.mk.backend.exception.ChatSessionNotFoundException;
import com.finki.ukim.mk.backend.exception.UserNotAssociatedWithSubjectException;
import com.finki.ukim.mk.backend.service.domain.AuthenticationService;
import com.finki.ukim.mk.backend.service.domain.ChatMessageService;
import com.finki.ukim.mk.backend.service.domain.ChatSessionService;
import com.finki.ukim.mk.backend.service.domain.LlmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatSessionServiceImpl implements ChatSessionService {

  private final ChatSessionRepository chatSessionRepository;
  private final AuthenticationService authenticationService;
  private final EnrollmentRepository enrollmentRepository;
  private final LlmService llmService;
  private final ChatMessageService chatMessageService;

  @Override
  public ChatSession openSession(Long subjectId) {
    User user = authenticationService.getCurrentUser();
    Enrollment enrollment = enrollmentRepository
      .findByUserIdAndSubjectId(user.getId(), subjectId)
      .orElseThrow(() -> new UserNotAssociatedWithSubjectException(subjectId));

    ChatSession chatSession = ChatSession
      .builder()
      .title("")
      .enrollment(enrollment)
      .build();

    return chatSessionRepository.save(chatSession);
  }


  @Override
  public ChatSession getSessionWithMessagesById(Long sessionId) {
    return chatSessionRepository
      .findByIdWithMessages(sessionId)
      .orElseThrow(() -> new ChatSessionNotFoundException(sessionId));
  }

  @Override
  public ChatMessage sendMessageInSession(ChatMessage message) {
    chatMessageService.save(message);
    ChatSession session = message.getSession();
    if (session.getMessages().size() == 1) {
      session.setTitle(message.getContent().length() > 20
        ? message.getContent().substring(0, 20) + "..."
        : message.getContent());
    }

    session.setUpdatedAt(OffsetDateTime.now());
    chatSessionRepository.save(session);

    ChatMessage receivedMessage = llmService.sendMessage(message);
    return chatMessageService.save(receivedMessage);
  }

  @Override
  public void deleteSession(Long sessionId) {
    chatSessionRepository.deleteById(sessionId);
  }

  @Override
  public ChatSession getSessionById(Long sessionId) {
    return chatSessionRepository.findById(sessionId).orElseThrow(() -> new ChatSessionNotFoundException(sessionId));
  }

  @Override
  public List<ChatSession> getSessionsBySubjectId(Long subjectId) {
    User user = authenticationService.getCurrentUser();
    Enrollment enrollment = enrollmentRepository.findByUserIdAndSubjectId(user.getId(), subjectId).orElseThrow(() -> new UserNotAssociatedWithSubjectException(subjectId));
    return enrollment.getChatSessions();
  }
}