package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.database.model.Enrollment;
import com.finki.ukim.mk.backend.database.model.Professor;
import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.database.model.Subject;
import com.finki.ukim.mk.backend.database.model.User;
import com.finki.ukim.mk.backend.database.repository.EnrollmentRepository;
import com.finki.ukim.mk.backend.database.repository.ProfessorGroupSubjectRepository;
import com.finki.ukim.mk.backend.database.repository.SubjectRepository;
import com.finki.ukim.mk.backend.exception.ProfessorAlreadyInGroupException;
import com.finki.ukim.mk.backend.exception.ProfessorGroupSubjectNotFoundException;
import com.finki.ukim.mk.backend.exception.SubjectNotFoundException;
import com.finki.ukim.mk.backend.exception.UnauthorizedUserException;
import com.finki.ukim.mk.backend.exception.UserAlreadyEnrolledException;
import com.finki.ukim.mk.backend.exception.UserNotEnrolledException;
import com.finki.ukim.mk.backend.service.domain.AuthenticationService;
import com.finki.ukim.mk.backend.service.domain.EnrollmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
  private final EnrollmentRepository enrollmentRepository;
  private final ProfessorGroupSubjectRepository professorGroupSubjectRepository;
  private final SubjectRepository subjectRepository;
  private final AuthenticationService authenticationService;

  @Override
  public Enrollment enrollAndCreateGroup(Long subjectId) {
    User currentUser = validateAndGetUser();
    Subject subject = validateAndGetSubject(subjectId);
    validateNotAlreadyEnrolled(currentUser, subject);

    Professor currentProfessor = currentUser.getProfessor();

    ProfessorGroupSubject professorGroupSubject = ProfessorGroupSubject.builder()
      .subject(subject)
      .shortName(currentProfessor.getShortName())
      .members(new HashSet<>(Collections.singletonList(currentProfessor)))
      .build();
    professorGroupSubjectRepository.save(professorGroupSubject);

    Enrollment enrollment = Enrollment.builder()
      .groupSubject(professorGroupSubject)
      .user(currentUser)
      .build();
    return enrollmentRepository.save(enrollment);
  }

  @Override
  public Enrollment enrollAndJoinGroup(Long subjectId, Long groupId) {
    User currentUser = validateAndGetUser();
    Subject subject = validateAndGetSubject(subjectId);
    validateNotAlreadyEnrolled(currentUser, subject);

    Professor professor = currentUser.getProfessor();

    ProfessorGroupSubject professorGroupSubject = professorGroupSubjectRepository.findById(groupId).orElseThrow(() -> new ProfessorGroupSubjectNotFoundException(groupId));
    if (professorGroupSubjectRepository.existsBySubjectAndMembersContains(subject, professor)) {
      throw new ProfessorAlreadyInGroupException(currentUser.getId(), subjectId);
    }
    professorGroupSubject.addProfessor(professor);
    professorGroupSubjectRepository.save(professorGroupSubject);

    Enrollment enrollment = Enrollment.builder()
      .groupSubject(professorGroupSubject)
      .user(currentUser)
      .build();
    return enrollmentRepository.save(enrollment);
  }

  @Override
  @Transactional
  public void unenroll(Long subjectId) {
    User currentUser = validateAndGetUser();

    Professor professor = currentUser.getProfessor();
    Enrollment enrollment = enrollmentRepository.findByUserIdAndSubjectId(professor.getId(), subjectId)
      .orElseThrow(() -> new UserNotEnrolledException(currentUser.getId(), subjectId));

    enrollmentRepository.delete(enrollment);

    ProfessorGroupSubject professorGroupSubject = enrollment.getGroupSubject();
    professorGroupSubject.removeProfessor(professor);

    if (professorGroupSubject.getMembers().isEmpty()) {
      professorGroupSubjectRepository.delete(professorGroupSubject);
    } else {
      professorGroupSubjectRepository.save(professorGroupSubject);
    }
  }


  private User validateAndGetUser() {
    User currentUser = authenticationService.getCurrentUser();
    if (!currentUser.isProfessor()) {
      throw new UnauthorizedUserException("Only professors can enroll themselves.");
    }
    return currentUser;
  }

  private Subject validateAndGetSubject(Long subjectId) {
    return subjectRepository.findById(subjectId).orElseThrow(() -> new SubjectNotFoundException(subjectId));
  }

  private void validateNotAlreadyEnrolled(User user, Subject subject) {
    if (enrollmentRepository.existsByUserIdAndGroupSubjectId(user.getId(), subject.getId())) {
      throw new UserAlreadyEnrolledException("User is already enrolled in this subject");
    }
  }
}
