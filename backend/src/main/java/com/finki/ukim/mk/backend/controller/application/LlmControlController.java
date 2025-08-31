package com.finki.ukim.mk.backend.controller.application;

import com.finki.ukim.mk.backend.dto.CreateLlmControlDto;
import com.finki.ukim.mk.backend.dto.LlmControlDto;
import com.finki.ukim.mk.backend.service.application.LlmControlApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/llm-controls")
@RequiredArgsConstructor
@Validated
public class LlmControlController {

  private final LlmControlApplicationService llmControlApplicationService;

  @GetMapping("/professor-group-subject/{professorGroupSubjectId}")
  public ResponseEntity<LlmControlDto> getByProfessorGroupSubject(
      @PathVariable("professorGroupSubjectId") @NotNull @Positive Long professorGroupSubjectId) {
    return ResponseEntity.ok(llmControlApplicationService.getLlmControlByGroupSubject(professorGroupSubjectId));
  }

  @PostMapping
  public ResponseEntity<LlmControlDto> create(@RequestBody @Valid CreateLlmControlDto newLlmControl) {
    LlmControlDto saved = llmControlApplicationService.saveLlmControl(newLlmControl);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PutMapping
  public ResponseEntity<LlmControlDto> update(@RequestBody @Valid LlmControlDto llmControl) {
    LlmControlDto updated = llmControlApplicationService.updateLlmControl(llmControl);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{llmControlId}")
  public ResponseEntity<Void> delete(
      @PathVariable("llmControlId") @NotNull @Positive Long llmControlId) {
    llmControlApplicationService.deleteLlmControlById(llmControlId);
    return ResponseEntity.noContent().build();
  }
}
