package com.finki.ukim.mk.backend.database.repository;

import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorGroupSubjectRepository extends JpaRepository<ProfessorGroupSubject, Long> {

  @Query("SELECT DISTINCT pgs FROM ProfessorGroupSubject pgs LEFT JOIN FETCH pgs.members WHERE pgs.id = :id")
  Optional<ProfessorGroupSubject> findByIdWithMembers(@Param("id") Long id);

  @Query("SELECT pgs FROM ProfessorGroupSubject pgs JOIN pgs.members m WHERE m.id = :professorId")
  List<ProfessorGroupSubject> findByProfessorId(@Param("professorId") Long professorId);

  @Query("SELECT DISTINCT pgs FROM ProfessorGroupSubject pgs LEFT JOIN FETCH pgs.members WHERE pgs.subject.id = :subjectId")
  List<ProfessorGroupSubject> findBySubjectIdWithMembers(@Param("subjectId") Long subjectId);
}
