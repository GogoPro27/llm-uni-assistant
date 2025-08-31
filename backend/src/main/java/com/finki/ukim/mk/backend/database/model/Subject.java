package com.finki.ukim.mk.backend.database.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"groupSubjects"})
@ToString(exclude = {"groupSubjects"})
public class Subject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "subject_id")
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(name = "short_name", nullable = false, unique = true)
  private String shortName;

  @Column(nullable = false, unique = true)
  private String code;

  @OneToMany(mappedBy = "subject", orphanRemoval = true, cascade = CascadeType.ALL)
  @Builder.Default
  private List<ProfessorGroupSubject> groupSubjects = new ArrayList<>();
}
