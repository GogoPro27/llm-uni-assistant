package com.finki.ukim.mk.backend.dto.light;

import com.finki.ukim.mk.backend.database.model.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnrollmentLightDto {
  private Long professorGroupId;
  private Long subjectId;
  private String subjectName;
  private String professorGroupName;

  public static EnrollmentLightDto fromEnrollment(Enrollment enrollment) {
    return new EnrollmentLightDto(
      enrollment.getGroupSubject().getId(),
      enrollment.getGroupSubject().getSubject().getId(),
      enrollment.getGroupSubject().getSubject().getName(),
      enrollment.getGroupSubject().getShortName()
    );
  }
}
