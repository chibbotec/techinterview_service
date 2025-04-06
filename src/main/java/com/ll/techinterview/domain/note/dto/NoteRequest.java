package com.ll.techinterview.domain.note.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NoteRequest {
  private String title;
  private String content;
  @JsonProperty("publicAccess")
  private boolean publicAccess;
}
