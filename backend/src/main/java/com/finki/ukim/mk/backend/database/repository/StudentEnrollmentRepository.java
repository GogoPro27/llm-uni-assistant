package com.finki.ukim.mk.backend.database.repository;

import com.finki.ukim.mk.backend.database.model.StudentEnrollment;
import com.finki.ukim.mk.backend.database.model.StudentEnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, StudentEnrollmentId> {
}
