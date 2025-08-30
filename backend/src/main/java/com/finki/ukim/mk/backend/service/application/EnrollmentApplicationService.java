package com.finki.ukim.mk.backend.service.application;

import com.finki.ukim.mk.backend.dto.EnrollmentProfessorGroupDetailsDto;

public interface EnrollmentApplicationService {
  EnrollmentProfessorGroupDetailsDto enrollAndCreateGroup(Long subjectId);
  EnrollmentProfessorGroupDetailsDto enrollAndJoinGroup(Long subjectId, Long groupId);
  void unenroll(Long subjectId);
}
