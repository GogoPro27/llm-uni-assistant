package com.finki.ukim.mk.backend.service.application.impl;

import com.finki.ukim.mk.backend.database.model.Enrollment;
import com.finki.ukim.mk.backend.dto.EnrollmentProfessorGroupDetailsDto;
import com.finki.ukim.mk.backend.service.application.EnrollmentApplicationService;
import com.finki.ukim.mk.backend.service.domain.EnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EnrollmentApplicationServiceImpl implements EnrollmentApplicationService {
  private final EnrollmentService enrollmentService;


  @Override
  public EnrollmentProfessorGroupDetailsDto enrollAndCreateGroup(Long subjectId) {
    Enrollment enrollment = enrollmentService.enrollAndCreateGroup(subjectId);
    return EnrollmentProfessorGroupDetailsDto.from(enrollment);
  }

  @Override
  public EnrollmentProfessorGroupDetailsDto enrollAndJoinGroup(Long subjectId, Long groupId) {
    Enrollment enrollment = enrollmentService.enrollAndJoinGroup(subjectId, groupId);
    return EnrollmentProfessorGroupDetailsDto.from(enrollment);
  }

  @Override
  public void unenroll(Long subjectId) {
    enrollmentService.unenroll(subjectId);
  }
}
