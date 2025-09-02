package com.finki.ukim.mk.backend.database.repository;

import com.finki.ukim.mk.backend.database.model.Professor;
import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.database.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorGroupSubjectRepository extends JpaRepository<ProfessorGroupSubject, Long> {
  Boolean existsBySubjectAndMembersContains(Subject subject, Professor professor);
  Optional<ProfessorGroupSubject> findBySubject_IdAndMembersContains(Long subjectId, Professor professor);
  List<ProfessorGroupSubject> findBySubject_Id(Long subjectId);
}
