package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.database.model.Enrollment;
import com.finki.ukim.mk.backend.database.model.EnrollmentId;
import com.finki.ukim.mk.backend.database.model.LlmControl;
import com.finki.ukim.mk.backend.database.model.Professor;
import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.database.model.Subject;
import com.finki.ukim.mk.backend.database.model.User;
import com.finki.ukim.mk.backend.database.repository.EnrollmentRepository;
import com.finki.ukim.mk.backend.database.repository.SubjectRepository;
import com.finki.ukim.mk.backend.exception.GroupDoesntBelongToSubjectException;
import com.finki.ukim.mk.backend.exception.ProfessorAlreadyInGroupException;
import com.finki.ukim.mk.backend.exception.SubjectNotFoundException;
import com.finki.ukim.mk.backend.exception.UnauthorizedUserException;
import com.finki.ukim.mk.backend.exception.UserAlreadyEnrolledException;
import com.finki.ukim.mk.backend.exception.UserNotEnrolledException;
import com.finki.ukim.mk.backend.factory.LlmControlFactory;
import com.finki.ukim.mk.backend.service.domain.AuthenticationService;
import com.finki.ukim.mk.backend.service.domain.EnrollmentService;
import com.finki.ukim.mk.backend.service.domain.LlmControlService;
import com.finki.ukim.mk.backend.service.domain.ProfessorGroupSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
  private final EnrollmentRepository enrollmentRepository;
  private final ProfessorGroupSubjectService professorGroupSubjectService;
  private final SubjectRepository subjectRepository;
  private final AuthenticationService authenticationService;
  private final LlmControlService llmControlService;
  private final LlmControlFactory llmControlFactory;

  @Override
  public Enrollment enrollAndCreateGroup(Long subjectId) {
    User currentUser = validateAndGetUser();
    Subject subject = validateAndGetSubject(subjectId);
    validateNotAlreadyEnrolled(currentUser, subject);

    Professor currentProfessor = currentUser.getProfessor();

    ProfessorGroupSubject professorGroupSubject = ProfessorGroupSubject.builder()
      .subject(subject)
      .members(new HashSet<>(Collections.singletonList(currentProfessor)))
      .build();

    professorGroupSubject.addProfessor(currentProfessor);

    LlmControl llmControl = llmControlFactory.create(professorGroupSubject);
    professorGroupSubject.setLlmControl(llmControl);

    llmControlService.saveLlmControl(llmControl);
    professorGroupSubjectService.save(professorGroupSubject);

    EnrollmentId enrollmentId = new EnrollmentId(currentUser.getId(), professorGroupSubject.getId());
    Enrollment enrollment = Enrollment.builder()
      .id(enrollmentId)
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

    ProfessorGroupSubject professorGroupSubject = professorGroupSubjectService.findById(groupId);

    if (!professorGroupSubject.getSubject().getId().equals(subjectId)) {
      throw new GroupDoesntBelongToSubjectException(groupId, subjectId);
    }

    if (professorGroupSubjectService.existsBySubjectAndMembersContains(subject)) {
      throw new ProfessorAlreadyInGroupException(currentUser.getId(), subjectId);
    }
    professorGroupSubject.addProfessor(professor);
    professorGroupSubjectService.save(professorGroupSubject);

    EnrollmentId enrollmentId = new EnrollmentId(currentUser.getId(), professorGroupSubject.getId());
    Enrollment enrollment = Enrollment.builder()
      .id(enrollmentId)
      .groupSubject(professorGroupSubject)
      .user(currentUser)
      .build();
    return enrollmentRepository.save(enrollment);
  }

  @Deprecated
  @Override
  public Enrollment enrollAsStudent(Long subjectId, Long groupId) {
    User currentUser = authenticationService.getCurrentUser();
    Subject subject = validateAndGetSubject(subjectId);
    validateNotAlreadyEnrolled(currentUser, subject);

    ProfessorGroupSubject professorGroupSubject = professorGroupSubjectService.findById(groupId);

    if (!professorGroupSubject.getSubject().getId().equals(subjectId)) {
      throw new GroupDoesntBelongToSubjectException(groupId, subjectId);
    }

    if (!currentUser.isStudent()) {
      throw new UnauthorizedUserException("This method can only be called by students");
    }

    EnrollmentId enrollmentId = new EnrollmentId(currentUser.getId(), professorGroupSubject.getId());
    Enrollment enrollment = Enrollment.builder()
      .id(enrollmentId)
      .groupSubject(professorGroupSubject)
      .user(currentUser)
      .build();


    return enrollmentRepository.save(enrollment);
  }

  @Override
  public void unenroll(Long subjectId) {
    User currentUser = validateAndGetUser();

    Professor professor = currentUser.getProfessor();
    Enrollment enrollment = enrollmentRepository.findByUserIdAndSubjectId(professor.getId(), subjectId)
      .orElseThrow(() -> new UserNotEnrolledException(currentUser.getId(), subjectId));

    enrollmentRepository.delete(enrollment);

    ProfessorGroupSubject professorGroupSubject = enrollment.getGroupSubject();
    professorGroupSubject.removeProfessor(professor);

    if (professorGroupSubject.getMembers().isEmpty()) {
      professorGroupSubjectService.deleteById(professorGroupSubject.getId());
    } else {
      professorGroupSubjectService.save(professorGroupSubject);
    }
  }

  @Override
  public List<Enrollment> getAllEnrollments() {
    User currentUser = authenticationService.getCurrentUser();
    return enrollmentRepository.findByUserId(currentUser.getId());
  }

  @Override
  public List<Subject> getSubjectsNotEnrolled() {
    List<Subject> subjectsEnrolled = getAllEnrollments().stream()
      .map(Enrollment::getGroupSubject)
      .map(ProfessorGroupSubject::getSubject)
      .toList();

    return subjectRepository.findAll().stream()
      .filter(subject -> !subjectsEnrolled.contains(subject))
      .toList();
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
    if (enrollmentRepository.existsByUserIdAndGroupSubject_SubjectId(user.getId(), subject.getId())) {
      throw new UserAlreadyEnrolledException(user.getId(), subject.getId());
    }
  }
}
