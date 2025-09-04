package com.finki.ukim.mk.backend.service.domain.impl;

import com.finki.ukim.mk.backend.database.model.LlmResource;
import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.database.model.enums.ResourceKind;
import com.finki.ukim.mk.backend.database.repository.LlmResourceRepository;
import com.finki.ukim.mk.backend.dto.storage.StorageObject;
import com.finki.ukim.mk.backend.service.domain.LlmResourceService;
import com.finki.ukim.mk.backend.service.domain.ProfessorGroupSubjectService;
import com.finki.ukim.mk.backend.service.domain.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LlmResourceServiceImpl implements LlmResourceService {

  private final LlmResourceRepository llmResourceRepository;
  private final StorageService storage;
  private final ProfessorGroupSubjectService professorGroupSubjectService;

  public LlmResource createLink(LlmResource llmResource) {
    return llmResourceRepository.save(llmResource);
  }

  public LlmResource uploadFile(Long groupSubjectId, MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File is required.");
    }
    ProfessorGroupSubject group = professorGroupSubjectService.findById(groupSubjectId);

    StorageObject stored;
    try (InputStream in = file.getInputStream()) {
      stored = storage.save(in, file.getOriginalFilename(), contentType(file), file.getSize());
    } catch (Exception e) {
      throw new RuntimeException("Failed to store file", e);
    }

    // Optional dedupe by checksum: reuse existing fileUri if present
    String finalFileUri = llmResourceRepository.findFirstByChecksum(stored.checksumSha256())
      .map(LlmResource::getFileUri)
      .orElse(stored.uri());

    // If we deduped and reused a different URI, delete the just-written blob to avoid orphans
    if (!finalFileUri.equals(stored.uri())) {
      try {
        storage.delete(stored.uri());
      } catch (Exception ignored) {
      }
    }

    LlmResource entity = LlmResource.builder()
      .groupSubject(group)
      .kind(ResourceKind.file)
      .title(file.getOriginalFilename())
      .description("")
      .mimeType(contentType(file))
      .sizeBytes(stored.sizeBytes())
      .checksum(stored.checksumSha256())
      .fileUri(finalFileUri)
      .build();

    return llmResourceRepository.save(entity);
  }

  public void deleteResource(Long resourceId, boolean deleteBlobIfUnreferenced) {
    LlmResource llmResource = llmResourceRepository.findById(resourceId)
      .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + resourceId));
    String uri = llmResource.getFileUri();
    llmResourceRepository.delete(llmResource);

    if (deleteBlobIfUnreferenced && uri != null && !uri.isBlank()) {
      boolean stillUsed = llmResourceRepository.existsByFileUri(uri);
      if (!stillUsed) {
        try {
          storage.delete(uri);
        } catch (Exception ignored) {
        }
      }
    }
  }


  public java.io.InputStream openFileStream(Long resourceId) {
    LlmResource llmResource = llmResourceRepository.findById(resourceId)
      .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + resourceId));
    if (llmResource.getKind() != ResourceKind.file) {
      throw new IllegalStateException("Not a file resource");
    }
    try {
      return storage.open(llmResource.getFileUri())
        .orElseThrow(() -> new IllegalStateException("Blob missing for resource " + resourceId));
    } catch (Exception e) {
      throw new RuntimeException("Open failed", e);
    }
  }

  @Override
  public List<LlmResource> listByGroup(Long groupSubjectId) {
    return llmResourceRepository.findAllByGroupSubject_Id(groupSubjectId);
  }

  @Override
  public LlmResource findById(Long resourceId) {
    return llmResourceRepository.findById(resourceId)
      .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + resourceId));
  }

  private static String contentType(MultipartFile f) {
    String ct = f.getContentType();
    return (ct == null || ct.isBlank()) ? "application/octet-stream" : ct;
  }

}