package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.database.model.Professor;
import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.database.model.Subject;
import com.finki.ukim.mk.backend.database.model.User;
import com.finki.ukim.mk.backend.database.repository.ProfessorGroupSubjectRepository;
import com.finki.ukim.mk.backend.exception.ProfessorAlreadyInGroupException;
import com.finki.ukim.mk.backend.exception.ProfessorGroupSubjectNotFoundException;
import com.finki.ukim.mk.backend.exception.UnauthorizedUserException;
import com.finki.ukim.mk.backend.service.domain.AuthenticationService;
import com.finki.ukim.mk.backend.service.domain.ProfessorGroupSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessorGroupSubjectServiceImpl implements ProfessorGroupSubjectService {
  private final ProfessorGroupSubjectRepository professorGroupSubjectRepository;
  private final AuthenticationService authenticationService;

  @Override
  public ProfessorGroupSubject findById(Long id) {
    return professorGroupSubjectRepository.findById(id).orElseThrow(() -> new ProfessorGroupSubjectNotFoundException(id));
  }

  @Override
  @Transactional
  public ProfessorGroupSubject save(ProfessorGroupSubject group) {
    return professorGroupSubjectRepository.save(group);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    if (!professorGroupSubjectRepository.existsById(id)) {
      throw new ProfessorGroupSubjectNotFoundException(id);
    }
    professorGroupSubjectRepository.deleteById(id);
  }

  @Override
  public Boolean existsBySubjectAndMembersContains(Subject subject) {
    Professor professor = validateAndGetCurrentProfessor();
    return professorGroupSubjectRepository.existsBySubjectAndMembersContains(subject, professor);
  }

  @Override
  @Transactional
  public ProfessorGroupSubject changeGroupForSubject(Long subjectId, Long newGroupId) {
    Professor professor = validateAndGetCurrentProfessor();

    ProfessorGroupSubject previousProfessorGroupSubject = professorGroupSubjectRepository
      .findBySubject_IdAndMembersContains(subjectId, professor)
      .orElseThrow(() -> new ProfessorGroupSubjectNotFoundException(subjectId, professor.getId()));

    if (previousProfessorGroupSubject.getId().equals(newGroupId)) {
      throw new ProfessorAlreadyInGroupException(professor.getId(), subjectId);
    }

    ProfessorGroupSubject professorGroupSubject = professorGroupSubjectRepository
      .findById(newGroupId)
      .orElseThrow(() -> new ProfessorGroupSubjectNotFoundException(newGroupId));

    previousProfessorGroupSubject.removeProfessor(professor);
    professorGroupSubject.addProfessor(professor);

    if (previousProfessorGroupSubject.getMembers().isEmpty()) {
      professorGroupSubjectRepository.delete(previousProfessorGroupSubject);
    } else {
      professorGroupSubjectRepository.save(previousProfessorGroupSubject);
    }
    return professorGroupSubjectRepository.save(professorGroupSubject);
  }

  private Professor validateAndGetCurrentProfessor() {
    User user = authenticationService.getCurrentUser();
    if (!user.isProfessor()) {
      throw new UnauthorizedUserException("Only professors can access this method.");
    }
    return user.getProfessor();
  }
}
