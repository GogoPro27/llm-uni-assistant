//package com.finki.ukim.mk.backend.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.ConstraintViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.NoSuchElementException;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//  @ExceptionHandler(MethodArgumentNotValidException.class)
//  public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//                                                               HttpServletRequest request) {
//    Map<String, String> validationErrors = new HashMap<>();
//    ex.getBindingResult().getFieldErrors()
//        .forEach(err -> validationErrors.put(err.getField(), err.getDefaultMessage()));
//    String message = "Validation failed for request body";
//    ApiError error = ApiError.of(HttpStatus.BAD_REQUEST, message, request.getRequestURI(), validationErrors);
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//  }
//
//  @ExceptionHandler(ConstraintViolationException.class)
//  public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex,
//                                                            HttpServletRequest request) {
//    Map<String, String> validationErrors = new HashMap<>();
//    ex.getConstraintViolations().forEach(v ->
//        validationErrors.put(v.getPropertyPath().toString(), v.getMessage()));
//    ApiError error = ApiError.of(HttpStatus.BAD_REQUEST, "Constraint violation", request.getRequestURI(), validationErrors);
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//  }
//
//  @ExceptionHandler(MissingServletRequestParameterException.class)
//  public ResponseEntity<ApiError> handleMissingParam(MissingServletRequestParameterException ex,
//                                                     HttpServletRequest request) {
//    ApiError error = ApiError.of(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//  }
//
//  @ExceptionHandler(NoSuchElementException.class)
//  public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex, HttpServletRequest request) {
//    ApiError error = ApiError.of(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
//    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//  }
//
//  @ExceptionHandler(IllegalArgumentException.class)
//  public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
//    ApiError error = ApiError.of(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//  }
//
//  @ExceptionHandler(IllegalStateException.class)
//  public ResponseEntity<ApiError> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
//    ApiError error = ApiError.of(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
//    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
//  }
//
//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
//    ApiError error = ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", request.getRequestURI());
//    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//  }
//
//  public record ApiError(Instant timestamp, int status, String error, String message, String path,
//                           Map<String, String> validationErrors) {
//      public static ApiError of(HttpStatus status, String message, String path) {
//        return new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, path, null);
//      }
//      public static ApiError of(HttpStatus status, String message, String path, Map<String, String> validationErrors) {
//        return new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, path, validationErrors);
//      }
//
//    }
//}
