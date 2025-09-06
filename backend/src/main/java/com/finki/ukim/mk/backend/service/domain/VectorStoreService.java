package com.finki.ukim.mk.backend.service.domain;

import org.springframework.core.io.Resource;

public interface VectorStoreService {
  void storeDocument(Resource resource, Long groupSubjectId);
}
