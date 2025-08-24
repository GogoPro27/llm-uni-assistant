package com.finki.ukim.mk.backend.database.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professor_group_subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessorGroupSubject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_subject_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "subject_id", nullable = false)
  private Subject subject;

  @Column(name = "short_name", nullable = false, unique = true)
  private String shortName;

  @ManyToMany
  @JoinTable(
    name = "professor_group_subject_members",
    joinColumns = @JoinColumn(name = "group_subject_id"),
    inverseJoinColumns = @JoinColumn(name = "professor_id")
  )
  @ToString.Exclude
  @Default
  private Set<Professor> members = new HashSet<>();

  @OneToOne(mappedBy = "groupSubject", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  private LlmControl llmControl;

  @OneToMany(mappedBy = "groupSubject", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @Default
  private Set<LlmResource> llmResources = new HashSet<>();

  public void addProfessor(Professor professor) {
    if (professor != null) {
      this.members.add(professor);
      professor.getGroupSubjects().add(this);
    }
  }

  public void removeProfessor(Professor professor) {
    if (professor != null) {
      this.members.remove(professor);
      professor.getGroupSubjects().remove(this);
    }
  }

}
