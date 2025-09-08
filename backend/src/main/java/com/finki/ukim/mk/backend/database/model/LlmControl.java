package com.finki.ukim.mk.backend.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "llm_controls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"groupSubject"})
@ToString(exclude = {"groupSubject"})
public class LlmControl {

  @Id
  @Column(name = "group_subject_id")
  private Long id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "group_subject_id")
  private ProfessorGroupSubject groupSubject;

  @Column(name = "model_name", nullable = false)
  private String modelName;

  @Column(name = "llm_provider", nullable = false)
  private String llmProvider;

  @Column(name = "instructions")
  private String instructions;

  @Builder.Default
  @Column(name = "params", columnDefinition = "jsonb", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  private String params = "{}";

  @Column(name = "strict_rag", nullable = false)
  private boolean strictRag;

  @Column(name = "relaxed_answers", nullable = false)
  private boolean relaxedAnswers;

  @Column(name = "top_k", nullable = false)
  private int topK;

  @Column(name = "similarity_threshold", nullable = false)
  private double similarityThreshold;

  @Column(name = "memory_window_size", nullable = false)
  private int memoryWindowSize;
}
