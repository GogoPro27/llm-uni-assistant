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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "llm_controls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LlmControl {

  @Id
  @Column(name = "group_subject_id")
  private Long id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "group_subject_id")
  private ProfessorGroupSubject groupSubject;

  @Column(name = "model_name")
  private String modelName;

  @Column(name = "instructions")
  private String instructions;

  @Column(name = "params", columnDefinition = "jsonb")
  private String params;
}
