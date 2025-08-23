package com.finki.ukim.mk.backend.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentEnrollment {

  @EmbeddedId
  private StudentEnrollmentId id;

  @ManyToOne
  @MapsId("studentId")
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  @ManyToOne
  @MapsId("groupSubjectId")
  @JoinColumn(name = "group_subject_id", nullable = false)
  private ProfessorGroupSubject groupSubject;

  @Column(name = "can_use_llm", nullable = false)
  private boolean canUseLlm;
}
