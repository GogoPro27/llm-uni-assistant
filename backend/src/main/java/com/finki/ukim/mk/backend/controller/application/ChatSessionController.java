package com.finki.ukim.mk.backend.controller.application;

import com.finki.ukim.mk.backend.dto.ChatMessageDto;
import com.finki.ukim.mk.backend.dto.ChatMessageRequestDto;
import com.finki.ukim.mk.backend.dto.ChatSessionWithMessagesDto;
import com.finki.ukim.mk.backend.dto.light.ChatSessionLightDto;
import com.finki.ukim.mk.backend.service.application.ChatSessionApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat-sessions")
@RequiredArgsConstructor
@Validated
public class ChatSessionController {

  private final ChatSessionApplicationService chatSessionApplicationService;

  @PostMapping("/subjects/{subjectId}/open")
  public ResponseEntity<ChatSessionWithMessagesDto> openSession(
    @PathVariable("subjectId") @NotNull @Positive Long subjectId) {
    ChatSessionWithMessagesDto session = chatSessionApplicationService.openSession(subjectId);
    return ResponseEntity.status(HttpStatus.CREATED).body(session);
  }

  @GetMapping("/{sessionId}")
  public ResponseEntity<ChatSessionWithMessagesDto> getSession(
    @PathVariable("sessionId") @NotNull @Positive Long sessionId) {
    return ResponseEntity.ok(chatSessionApplicationService.getSessionWithMessagesById(sessionId));
  }

  @PostMapping("/messages")
  public ResponseEntity<ChatMessageDto> sendMessage(@RequestBody @Valid ChatMessageRequestDto message) {
    ChatMessageDto created = chatSessionApplicationService.sendMessageInSession(message);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @DeleteMapping("/{sessionId}")
  public ResponseEntity<Void> deleteSession(
    @PathVariable("sessionId") @NotNull @Positive Long sessionId) {
    chatSessionApplicationService.deleteSession(sessionId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/subjects/{subjectId}")
  public ResponseEntity<List<ChatSessionLightDto>> getSessionsBySubjectId(
    @PathVariable("subjectId") @NotNull @Positive Long subjectId) {
    List<ChatSessionLightDto> sessions = chatSessionApplicationService.getSessionsBySubjectId(subjectId);
    return ResponseEntity.ok(sessions);
  }
}
