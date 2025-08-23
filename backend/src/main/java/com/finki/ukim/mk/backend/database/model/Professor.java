package com.finki.ukim.mk.backend.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professor {

  @Id
  @Column(name = "professor_id")
  private Long id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "professor_id")
  @ToString.Exclude
  private User user;

  @Column(name = "short_name", nullable = false)
  private String shortName;

  @ManyToMany(mappedBy = "members")
  @ToString.Exclude
  private Set<ProfessorGroupSubject> groupSubjects = new HashSet<>();
}
