package com.finki.ukim.mk.backend.dto;

import com.finki.ukim.mk.backend.database.model.LlmResource;
import com.finki.ukim.mk.backend.database.model.enums.ResourceKind;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LlmResourceMetadataDto {
  private Long id;
  private ResourceKind kind;
  private String title;
  private String mimeType;  // for FILE
  private Long sizeBytes;   // for FILE
  private String url;       // for LINK

  public static LlmResourceMetadataDto fromLlmResource(LlmResource llmResource) {
    return new LlmResourceMetadataDto(
      llmResource.getId(),
      llmResource.getKind(),
      llmResource.getTitle(),
      llmResource.getMimeType(),
      llmResource.getSizeBytes(),
      llmResource.getUrl()
    );
  }
}