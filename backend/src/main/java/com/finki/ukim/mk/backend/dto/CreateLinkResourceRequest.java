package com.finki.ukim.mk.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class CreateLinkResourceRequest {
  private Long groupSubjectId;
  private String title;
  private String url;
  private String description;
}