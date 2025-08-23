package com.finki.ukim.mk.backend.database.repository;

import com.finki.ukim.mk.backend.database.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
