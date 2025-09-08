package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.dto.light.LlmResourceLightDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfessorGroupSubjectDto {
  private Long id;
  private String shortName;
  private String subjectName;
  private List<String> professorMembers;
  private LlmControlDto llmControl;
  private List<LlmResourceLightDto> llmResources;

  public static ProfessorGroupSubjectDto fromProfessorGroupSubject(ProfessorGroupSubject professorGroupSubject) {
    return new ProfessorGroupSubjectDto(
      professorGroupSubject.getId(),
      professorGroupSubject.getShortName(),
      professorGroupSubject.getSubject().getName(),
      professorGroupSubject.getMembers().stream().map(professor -> {
        String name = professor.getUser().getName();
        String surname = professor.getUser().getSurname();
        return name + " " + surname;
      }).toList(),
      professorGroupSubject.getLlmControl() != null ? LlmControlDto.fromLlmControl(professorGroupSubject.getLlmControl()) : null,
      professorGroupSubject.getLlmResources().stream().map(LlmResourceLightDto::fromLlmResource).toList()
    );
  }
}
