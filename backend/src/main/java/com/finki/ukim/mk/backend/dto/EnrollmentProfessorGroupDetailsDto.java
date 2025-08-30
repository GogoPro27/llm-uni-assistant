package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnrollmentProfessorGroupDetailsDto {
  Long professorGroupSubjectId;
  Long subjectId;
  Boolean isChatAvailable;
  String subjectShortName;
  String professorGroupShortName;

  public static EnrollmentProfessorGroupDetailsDto from(Enrollment enrollment) {
    return new EnrollmentProfessorGroupDetailsDto(
      enrollment.getGroupSubject().getId(),
      enrollment.getGroupSubject().getSubject().getId(),
      enrollment.isCanUseLlm(),
      enrollment.getGroupSubject().getSubject().getShortName(),
      enrollment.getGroupSubject().getShortName()
    );
  }
}
