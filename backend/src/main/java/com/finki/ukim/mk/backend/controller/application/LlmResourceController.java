package com.finki.ukim.mk.backend.controller.application;

import com.finki.ukim.mk.backend.database.model.enums.ResourceKind;
import com.finki.ukim.mk.backend.dto.CreateLinkResourceRequest;
import com.finki.ukim.mk.backend.dto.LlmResourceMetadataDto;
import com.finki.ukim.mk.backend.dto.ResourceDto;
import com.finki.ukim.mk.backend.dto.light.LlmResourceLightDto;
import com.finki.ukim.mk.backend.service.application.LlmResourceApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
@Validated
public class LlmResourceController {

  private final LlmResourceApplicationService llmResourceApplicationService;

  @PostMapping(
    path = "/upload",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResourceDto uploadFile(
    @RequestParam("groupSubjectId") Long groupSubjectId,
    @RequestPart("file") MultipartFile file
  ) {
    return llmResourceApplicationService.uploadFile(groupSubjectId, file);
  }

  @PostMapping(
    path = "/link",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResourceDto createLink(@RequestBody CreateLinkResourceRequest request) {
    return llmResourceApplicationService.createLink(request);
  }

  @GetMapping("/group/{groupId}")
  public List<LlmResourceLightDto> listByGroup(@PathVariable("groupId") Long groupId) {
    return llmResourceApplicationService.listByGroup(groupId);
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<StreamingResponseBody> download(@PathVariable Long id) {
    LlmResourceMetadataDto meta = llmResourceApplicationService.getMeta(id);

    if (!meta.getKind().equals(ResourceKind.file)) {
      return ResponseEntity.badRequest().build();
    }

    StreamingResponseBody body = out -> {
      try (InputStream in = llmResourceApplicationService.openFileStream(id)) {
        in.transferTo(out);
      }
    };
    String filename = safeFilename(meta.getTitle());
    String contentDisposition = contentDispositionAttachment(filename);
    MediaType mediaType = parseMediaTypeOrOctet(meta.getMimeType());

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
      .contentType(mediaType)
      .contentLength(meta.getSizeBytes() != null ? meta.getSizeBytes() : -1)
      .body(body);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
    @PathVariable Long id,
    @RequestParam(name = "deleteBlobIfUnreferenced", defaultValue = "true") boolean deleteBlobIfUnreferenced) {

    llmResourceApplicationService.deleteResource(id, deleteBlobIfUnreferenced);
    return ResponseEntity.noContent().build();
  }

  private static String safeFilename(String name) {
    if (!StringUtils.hasText(name)) return "file";
    return name.replaceAll("[\\r\\n\"]", "_");
  }

  private static String contentDispositionAttachment(String filename) {
    String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    return "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + encoded;
  }

  private static MediaType parseMediaTypeOrOctet(String mt) {
    try {
      return (StringUtils.hasText(mt)) ? MediaType.parseMediaType(mt) : MediaType.APPLICATION_OCTET_STREAM;
    } catch (Exception e) {
      return MediaType.APPLICATION_OCTET_STREAM;
    }
  }
}