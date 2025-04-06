package com.ll.techinterview.domain.note.dto;

import com.ll.techinterview.domain.note.document.Note;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorizedNotesResponse {
  private List<NoteResponse> publicNotes;
  private List<NoteResponse> privateNotes;

  public static CategorizedNotesResponse of(List<Note> publicNotes, List<Note> privateNotes) {
    return CategorizedNotesResponse.builder()
        .publicNotes(publicNotes.stream().map(NoteResponse::of).toList())
        .privateNotes(privateNotes.stream().map(NoteResponse::of).toList())
        .build();
  }
}
