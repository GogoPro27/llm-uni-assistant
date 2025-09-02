package com.finki.ukim.mk.backend.controller.application;

import com.finki.ukim.mk.backend.dto.ProfessorGroupSubjectDto;
import com.finki.ukim.mk.backend.dto.light.ProfessorGroupSubjectLightDto;
import com.finki.ukim.mk.backend.dto.light.SubjectLightDto;
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

import java.util.List;

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

  @GetMapping("/enrolled-subjects")
  public ResponseEntity<List<SubjectLightDto>> getEnrolledSubjects() {
    List<SubjectLightDto> enrolledSubjects = professorGroupSubjectApplicationService.getEnrolledSubjects();
    return ResponseEntity.ok(enrolledSubjects);
  }

  @GetMapping("/subjects/{subjectId}/groups")
  public ResponseEntity<List<ProfessorGroupSubjectLightDto>> getAllGroupsBySubjectId(
    @PathVariable("subjectId") @NotNull @Positive Long subjectId) {
    List<ProfessorGroupSubjectLightDto> groups =
      professorGroupSubjectApplicationService.getAllGroupsBySubjectId(subjectId);
    return ResponseEntity.ok(groups);
  }
}
