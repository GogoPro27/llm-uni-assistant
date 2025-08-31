package com.finki.ukim.mk.backend.controller.application;

import com.finki.ukim.mk.backend.dto.ProfessorGroupSubjectDto;
import com.finki.ukim.mk.backend.service.application.ProfessorGroupSubjectApplicationService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/professor-group-subjects")
@RequiredArgsConstructor
@Validated
public class ProfessorGroupSubjectController {

  private final ProfessorGroupSubjectApplicationService professorGroupSubjectApplicationService;

  @GetMapping("/{id}")
  public ResponseEntity<ProfessorGroupSubjectDto> findById(
      @PathVariable("id") @NotNull @Positive Long id) {
    return ResponseEntity.ok(professorGroupSubjectApplicationService.findById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(
      @PathVariable("id") @NotNull @Positive Long id) {
    professorGroupSubjectApplicationService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/subjects/{subjectId}/change-group/{newGroupId}")
  public ResponseEntity<ProfessorGroupSubjectDto> changeGroupForSubject(
      @PathVariable("subjectId") @NotNull @Positive Long subjectId,
      @PathVariable("newGroupId") @NotNull @Positive Long newGroupId) {
    ProfessorGroupSubjectDto updated =
        professorGroupSubjectApplicationService.changeGroupForSubject(subjectId, newGroupId);
    return ResponseEntity.ok(updated);
  }
}
