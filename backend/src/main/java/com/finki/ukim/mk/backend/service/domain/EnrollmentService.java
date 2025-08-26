package com.finki.ukim.mk.backend.service.domain;

import com.finki.ukim.mk.backend.database.model.Enrollment;

public interface EnrollmentService {
  Enrollment enrollAndCreateGroup(Long subjectId);
  Enrollment enrollAndJoinGroup(Long subjectId, Long groupId);
  void unenroll(Long subjectId);
}
