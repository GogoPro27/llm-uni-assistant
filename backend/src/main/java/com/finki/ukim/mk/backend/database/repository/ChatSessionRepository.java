package com.finki.ukim.mk.backend.database.repository;

import com.finki.ukim.mk.backend.database.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

  @Query("SELECT cs FROM ChatSession cs LEFT JOIN FETCH cs.messages WHERE cs.id = :id")
  Optional<ChatSession> findByIdWithMessages(@Param("id") Long id);

}
