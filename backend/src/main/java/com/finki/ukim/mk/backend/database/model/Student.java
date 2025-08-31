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

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
public class Student {

  @Id
  @Column(name = "student_id")
  private Long id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "student_id")
  private User user;

  @Column(name = "student_index", nullable = false, unique = true)
  private Long studentIndex;
}
