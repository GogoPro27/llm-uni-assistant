package com.finki.ukim.mk.backend.service.application;

import com.finki.ukim.mk.backend.dto.EnrollmentProfessorGroupDetailsDto;
import com.finki.ukim.mk.backend.dto.light.EnrollmentLightDto;
import com.finki.ukim.mk.backend.dto.light.SubjectLightDto;

import java.util.List;

public interface EnrollmentApplicationService {
  EnrollmentProfessorGroupDetailsDto enrollAndCreateGroup(Long subjectId);
  EnrollmentProfessorGroupDetailsDto enrollAndJoinGroup(Long subjectId, Long groupId);
  EnrollmentProfessorGroupDetailsDto enrollAsStudent(Long subjectId, Long groupId);
  void unenroll(Long subjectId);
  List<EnrollmentLightDto> getEnrolledSubjects();
  List<SubjectLightDto> getSubjectsNotEnrolled();
}