package com.finki.ukim.mk.backend.dto.light;

import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfessorGroupSubjectLightDto {
  private Long id;
  private String shortName;

  public static ProfessorGroupSubjectLightDto fromProfessorGroupSubject(ProfessorGroupSubject professorGroupSubject) {
    return new ProfessorGroupSubjectLightDto(
      professorGroupSubject.getId(),
      professorGroupSubject.getShortName()
    );
  }
}
