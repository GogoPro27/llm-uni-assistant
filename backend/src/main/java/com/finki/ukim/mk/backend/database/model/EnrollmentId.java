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
public class EnrollmentId implements Serializable {

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "group_subject_id")
  private Long groupSubjectId;
}
