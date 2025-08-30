package com.finki.ukim.mk.backend.exception;

public class GroupDoesntBelongToSubjectException extends RuntimeException{
  public GroupDoesntBelongToSubjectException(Long groupId,Long subjectId) {
    super("Group with id: " + groupId + " doesn't belong to subject with id: " + subjectId);
  }
}
