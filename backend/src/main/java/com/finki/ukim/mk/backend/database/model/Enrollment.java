package com.finki.ukim.mk.backend.database.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"user", "groupSubject", "chatSessions"})
@ToString(exclude = {"user", "groupSubject", "chatSessions"})
public class Enrollment {

  @EmbeddedId
  private EnrollmentId id;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @MapsId("groupSubjectId")
  @JoinColumn(name = "group_subject_id", nullable = false)
  private ProfessorGroupSubject groupSubject;

  @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
  @Default
  private List<ChatSession> chatSessions = new ArrayList<>();

  @Column(name = "can_use_llm", nullable = false)
  @Default
  private boolean canUseLlm = false;
}
