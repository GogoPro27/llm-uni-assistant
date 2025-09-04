package com.finki.ukim.mk.backend.service.domain;

import com.finki.ukim.mk.backend.database.model.LlmResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LlmResourceService {
  LlmResource createLink(LlmResource req);
  LlmResource uploadFile(Long groupSubjectId, MultipartFile file);
  void deleteResource(Long resourceId, boolean deleteBlobIfUnreferenced);
  java.io.InputStream openFileStream(Long resourceId);
  List<LlmResource> listByGroup(Long groupSubjectId);
  LlmResource findById(Long resourceId);
}