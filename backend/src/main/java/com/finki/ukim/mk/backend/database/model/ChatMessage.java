package com.finki.ukim.mk.backend.database.model;

import com.finki.ukim.mk.backend.database.model.enums.MessageOrigin;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "message_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "session_id", nullable = false)
  private ChatSession session;

  @Enumerated(EnumType.STRING)
  @Column(name = "origin", nullable = false, columnDefinition = "message_origin")
  private MessageOrigin origin;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "created_at")
  private OffsetDateTime createdAt;
}
