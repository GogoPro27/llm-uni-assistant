package com.finki.ukim.mk.backend.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
  
  @OneToMany(mappedBy = "enrollment", fetch = FetchType.LAZY)
  @Default
  private List<ChatSession> chatSessions = new ArrayList<>();
  
  @Column(name = "can_use_llm", nullable = false)
  @Default
  private boolean canUseLlm = false;
}
