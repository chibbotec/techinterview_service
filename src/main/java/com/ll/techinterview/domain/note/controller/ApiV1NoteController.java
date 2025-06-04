package com.ll.techinterview.domain.note.controller;


import com.ll.techinterview.domain.note.dto.CategorizedNotesResponse;
import com.ll.techinterview.domain.note.dto.NoteRequest;
import com.ll.techinterview.domain.note.dto.NoteResponse;
import com.ll.techinterview.domain.note.service.NoteService;
import com.ll.techinterview.global.client.MemberResponse;
import com.ll.techinterview.global.webMvc.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tech-interview/{spaceId}/notes")
@RequiredArgsConstructor
@Slf4j
public class ApiV1NoteController {

  private final NoteService noteService;

  @GetMapping
  public ResponseEntity<CategorizedNotesResponse> getNoteList(
      @PathVariable("spaceId") Long spaceId,
      @LoginUser MemberResponse loginUser
  ){
    return ResponseEntity.ok(noteService.getNoteList(spaceId, loginUser));
  }

  @GetMapping("/{noteId}")
  public ResponseEntity<NoteResponse> getNote(
      @PathVariable("noteId") String noteId
  ){
    return ResponseEntity.ok(noteService.getNote(noteId));
  }

  @PostMapping
  public ResponseEntity<NoteResponse> createNote(
      @PathVariable("spaceId") Long spaceId,
      @LoginUser MemberResponse loginUser,
      @RequestBody NoteRequest noteRequest
  ){
    return ResponseEntity.ok(noteService.createNote(spaceId, loginUser, noteRequest));
  }

  @PutMapping("/{noteId}")
  public ResponseEntity<NoteResponse> updateNote(
      @PathVariable("noteId") String noteId,
      @LoginUser MemberResponse loginUser,
      @RequestBody NoteRequest noteRequest
  ){
    return ResponseEntity.ok(noteService.updateNote(noteId, loginUser, noteRequest));
  }

  @DeleteMapping("/{noteId}")
  public ResponseEntity deleteNote(
      @PathVariable("noteId") String noteId,
      @LoginUser MemberResponse loginUser
  ){
    noteService.deleteNote(noteId, loginUser);
    return ResponseEntity.ok().build();
  }

}
