package com.finki.ukim.mk.backend.database.model;

import com.finki.ukim.mk.backend.database.model.enums.ResourceKind;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "llm_resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LlmResource {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "resource_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "group_subject_id", nullable = false)
  private ProfessorGroupSubject groupSubject;

  @Enumerated(EnumType.STRING)
  @Column(name = "kind", nullable = false, columnDefinition = "resource_kind")
  private ResourceKind kind;

  @Column(nullable = false)
  private String title;

  @Column(name = "url")
  private String url;

  @Column(name = "file_uri")
  private String fileUri;

  @Column(name = "mime_type")
  private String mimeType;

  @Column(name = "size_bytes")
  private Long sizeBytes;

  @Column(name = "checksum")
  private String checksum;

  @Column(name = "description")
  private String description;
}
