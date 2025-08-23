package com.finki.ukim.mk.backend.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrollmentId implements Serializable {

  @Column(name = "student_id")
  private Long studentId;

  @Column(name = "group_subject_id")
  private Long groupSubjectId;
}
