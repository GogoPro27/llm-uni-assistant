package com.finki.ukim.mk.backend.service.domain;

import com.finki.ukim.mk.backend.database.model.Enrollment;
import com.finki.ukim.mk.backend.database.model.Subject;

import java.util.List;

public interface EnrollmentService {
  Enrollment enrollAndCreateGroup(Long subjectId);
  Enrollment enrollAndJoinGroup(Long subjectId, Long groupId);
  void unenroll(Long subjectId);
  List<Enrollment> getAllEnrollments();
  List<Subject> getSubjectsNotEnrolled();
}
