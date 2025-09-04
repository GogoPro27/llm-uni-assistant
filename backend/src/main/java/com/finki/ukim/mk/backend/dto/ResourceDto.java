package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.LlmResource;
import com.finki.ukim.mk.backend.database.model.enums.ResourceKind;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResourceDto {
  private Long id;
  private Long groupSubjectId;
  private ResourceKind kind;
  private String title;
  private String description;
  private String url; // for links
  private String mimeType;
  private Long sizeBytes;
  private String checksum;
  private String fileUri;

  public static ResourceDto toDto(LlmResource e) {
    return new ResourceDto(
      e.getId(),
      e.getGroupSubject().getId(),
      e.getKind(),
      e.getTitle(),
      e.getDescription(),
      e.getUrl(),
      e.getMimeType(),
      e.getSizeBytes(),
      e.getChecksum(),
      e.getFileUri()
    );
  }
}