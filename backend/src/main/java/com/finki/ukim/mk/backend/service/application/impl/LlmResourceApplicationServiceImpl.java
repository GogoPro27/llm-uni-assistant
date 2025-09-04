package com.finki.ukim.mk.backend.service.application.impl;

import com.finki.ukim.mk.backend.database.model.LlmResource;
import com.finki.ukim.mk.backend.database.model.ProfessorGroupSubject;
import com.finki.ukim.mk.backend.database.model.enums.ResourceKind;
import com.finki.ukim.mk.backend.dto.CreateLinkResourceRequest;
import com.finki.ukim.mk.backend.dto.ResourceDto;
import com.finki.ukim.mk.backend.dto.LlmResourceMetadataDto;
import com.finki.ukim.mk.backend.dto.light.LlmResourceLightDto;
import com.finki.ukim.mk.backend.service.application.LlmResourceApplicationService;
import com.finki.ukim.mk.backend.service.domain.LlmResourceService;
import com.finki.ukim.mk.backend.service.domain.ProfessorGroupSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LlmResourceApplicationServiceImpl implements LlmResourceApplicationService {
  private final LlmResourceService llmResourceService;
  private final ProfessorGroupSubjectService professorGroupSubjectService;

  @Override
  public ResourceDto createLink(CreateLinkResourceRequest req) {
    ProfessorGroupSubject group = professorGroupSubjectService.findById(req.getGroupSubjectId());

    LlmResource entity = LlmResource.builder()
      .groupSubject(group)
      .kind(ResourceKind.link)
      .title(req.getTitle())
      .description(req.getDescription())
      .url(req.getUrl())
      .build();
    return ResourceDto.toDto(llmResourceService.createLink(entity));
  }

  @Override
  public ResourceDto uploadFile(Long groupSubjectId, MultipartFile file) {

    return ResourceDto.toDto(llmResourceService.uploadFile(groupSubjectId, file));
  }

  @Override
  public void deleteResource(Long resourceId, boolean deleteBlobIfUnreferenced) {
    llmResourceService.deleteResource(resourceId, deleteBlobIfUnreferenced);
  }

  @Override
  public InputStream openFileStream(Long resourceId) {
    return llmResourceService.openFileStream(resourceId);
  }

  @Override
  public List<LlmResourceLightDto> listByGroup(Long groupSubjectId) {
    return llmResourceService.listByGroup(groupSubjectId).stream()
      .map(LlmResourceLightDto::fromLlmResource)
      .toList();
  }

  @Override
  public LlmResourceMetadataDto getMeta(Long resourceId) {
    LlmResource res = llmResourceService.findById(resourceId);
    return LlmResourceMetadataDto.fromLlmResource(res);
  }
}
