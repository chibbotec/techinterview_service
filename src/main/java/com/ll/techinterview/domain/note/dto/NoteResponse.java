package com.ll.techinterview.domain.note.dto;

import com.ll.techinterview.domain.note.document.Note;
import com.ll.techinterview.domain.note.document.Note.Author;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {
  private String id;
  private Long spaceId;
  private String title;
  private String content;
  private Author author;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean publicAccess;

  public static NoteResponse of(Note note) {
    return NoteResponse.builder()
        .id(note.getId())
        .spaceId(note.getSpaceId())
        .title(note.getTitle())
        .content(note.getContent())
        .author(note.getAuthor())
        .createdAt(note.getCreatedAt())
        .updatedAt(note.getUpdatedAt())
        .publicAccess(note.isPublicAccess())
        .build();
  }
}
