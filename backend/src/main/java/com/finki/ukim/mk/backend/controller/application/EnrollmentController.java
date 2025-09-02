package com.finki.ukim.mk.backend.controller.application;

import com.finki.ukim.mk.backend.dto.EnrollmentProfessorGroupDetailsDto;
import com.finki.ukim.mk.backend.dto.light.EnrollmentLightDto;
import com.finki.ukim.mk.backend.dto.light.SubjectLightDto;
import com.finki.ukim.mk.backend.service.application.EnrollmentApplicationService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Validated
public class EnrollmentController {

  private final EnrollmentApplicationService enrollmentApplicationService;

  @GetMapping
  public ResponseEntity<List<EnrollmentLightDto>> getEnrolledSubjects() {
    List<EnrollmentLightDto> enrolledSubjects = enrollmentApplicationService.getEnrolledSubjects();
    return ResponseEntity.ok(enrolledSubjects);
  }

  @GetMapping("/subjects/not-enrolled")
  public ResponseEntity<List<SubjectLightDto>> getSubjectsNotEnrolled() {
    List<SubjectLightDto> subjectsNotEnrolled = enrollmentApplicationService.getSubjectsNotEnrolled();
    return ResponseEntity.ok(subjectsNotEnrolled);
  }

  @PostMapping("/subjects/{subjectId}/create-group")
  public ResponseEntity<EnrollmentProfessorGroupDetailsDto> enrollAndCreateGroup(
    @PathVariable("subjectId") @NotNull @Positive Long subjectId) {
    EnrollmentProfessorGroupDetailsDto result = enrollmentApplicationService.enrollAndCreateGroup(subjectId);
    return ResponseEntity.ok(result);
  }

  @PostMapping("/subjects/{subjectId}/join-group/{groupId}")
  public ResponseEntity<EnrollmentProfessorGroupDetailsDto> enrollAndJoinGroup(
    @PathVariable("subjectId") @NotNull @Positive Long subjectId,
    @PathVariable("groupId") @NotNull @Positive Long groupId) {
    EnrollmentProfessorGroupDetailsDto result = enrollmentApplicationService.enrollAndJoinGroup(subjectId, groupId);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/subjects/{subjectId}")
  public ResponseEntity<Void> unenroll(
    @PathVariable("subjectId") @NotNull @Positive Long subjectId) {
    enrollmentApplicationService.unenroll(subjectId);
    return ResponseEntity.noContent().build();
  }
}
