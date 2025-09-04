package com.finki.ukim.mk.backend.dto.light;

import com.finki.ukim.mk.backend.database.model.LlmResource;
import com.finki.ukim.mk.backend.database.model.enums.ResourceKind;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LlmResourceLightDto {
  private Long id;
  private ResourceKind kind;
  private String title;
  private String description;
  private String url;      // for kind=LINK
  private String fileUri;  // optional — you may want to hide it and instead expose a /download/{id} endpoint
  private String mimeType;

  public static LlmResourceLightDto fromLlmResource(LlmResource llmResource) {
    return new LlmResourceLightDto(
      llmResource.getId(),
      llmResource.getKind(),
      llmResource.getTitle(),
      llmResource.getDescription(),
      llmResource.getUrl(),
      llmResource.getFileUri(),
      llmResource.getMimeType()
    );
  }
}
