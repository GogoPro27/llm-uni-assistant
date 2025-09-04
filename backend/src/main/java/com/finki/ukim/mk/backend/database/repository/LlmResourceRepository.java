package com.finki.ukim.mk.backend.database.repository;

import com.finki.ukim.mk.backend.database.model.LlmResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LlmResourceRepository extends JpaRepository<LlmResource, Long> {
  Optional<LlmResource> findFirstByChecksum(String checksum);
  boolean existsByFileUri(String fileUri);
  List<LlmResource> findAllByGroupSubject_Id(Long groupSubjectId);
}
