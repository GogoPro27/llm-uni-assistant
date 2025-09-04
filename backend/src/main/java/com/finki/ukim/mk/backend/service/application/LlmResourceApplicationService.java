package com.finki.ukim.mk.backend.service.application;

import com.finki.ukim.mk.backend.dto.CreateLinkResourceRequest;
import com.finki.ukim.mk.backend.dto.ResourceDto;
import com.finki.ukim.mk.backend.dto.LlmResourceMetadataDto;
import com.finki.ukim.mk.backend.dto.light.LlmResourceLightDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LlmResourceApplicationService {
  ResourceDto createLink(CreateLinkResourceRequest req);
  ResourceDto uploadFile(Long groupSubjectId, MultipartFile file);
  void deleteResource(Long resourceId, boolean deleteBlobIfUnreferenced);
  java.io.InputStream openFileStream(Long resourceId);
  List<LlmResourceLightDto> listByGroup(Long groupSubjectId);
  LlmResourceMetadataDto getMeta(Long resourceId);
}