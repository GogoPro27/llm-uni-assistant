package com.finki.ukim.mk.backend.database.repository;

import com.finki.ukim.mk.backend.database.model.Enrollment;
import com.finki.ukim.mk.backend.database.model.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {

  @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId AND e.groupSubject.subject.id = :subjectId")
  Optional<Enrollment> findByUserIdAndSubjectId(@Param("userId") Long userId, @Param("subjectId") Long subjectId);

  Boolean existsByUserIdAndGroupSubject_SubjectId(Long userId, Long groupSubjectId);
  List<Enrollment> findByUserId(Long userId);
}
