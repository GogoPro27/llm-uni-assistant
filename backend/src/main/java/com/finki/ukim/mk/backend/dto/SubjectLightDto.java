package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubjectLightDto {
  private Long subjectId;
  private String subjectShortName;

  public static SubjectLightDto fromSubject(Subject subject) {
    return new SubjectLightDto(
      subject.getId(),
      subject.getShortName()
    );
  }
}
