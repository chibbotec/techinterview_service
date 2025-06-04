package com.ll.techinterview.domain.note.service;

import com.ll.techinterview.domain.note.document.Note;
import com.ll.techinterview.domain.note.document.Note.Author;
import com.ll.techinterview.domain.note.dto.CategorizedNotesResponse;
import com.ll.techinterview.domain.note.dto.NoteRequest;
import com.ll.techinterview.domain.note.dto.NoteResponse;
import com.ll.techinterview.domain.note.repository.NoteRepository;
import com.ll.techinterview.global.client.MemberResponse;
import com.ll.techinterview.global.error.ErrorCode;
import com.ll.techinterview.global.exception.CustomException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NoteService {

  private final NoteRepository noteRepository;

  public CategorizedNotesResponse getNoteList(Long spaceId, MemberResponse loginUser) {
    //public note
    List<Note> publicNotes = noteRepository.findAllBySpaceIdAndPublicAccess(spaceId, true);
    //private note
    List<Note> privateNotes = noteRepository.findAllBySpaceIdAndAuthor_Id(spaceId, loginUser.getId());

    return CategorizedNotesResponse.of(publicNotes, privateNotes);
  }

  public NoteResponse getNote(String noteId) {
    Note note = noteRepository.findById(noteId)
        .orElseThrow(() -> new IllegalArgumentException("Note not found"));

    return NoteResponse.of(note);
  }

  @Transactional
  public NoteResponse createNote(Long spaceId, MemberResponse loginUser, NoteRequest noteRequest) {
    log.info("Create note");
    log.info("spaceId: {}", spaceId);
    log.info("loginUser: {}", loginUser);
    log.info("noteRequest: {}", noteRequest.isPublicAccess());

    Note note = Note.builder()
        .spaceId(spaceId)
        .title(noteRequest.getTitle())
        .content(noteRequest.getContent())
        .author(Author.builder()
            .id(loginUser.getId())
            .nickname(loginUser.getNickname())
            .build())
        .publicAccess(noteRequest.isPublicAccess())
        .build();

    return NoteResponse.of(noteRepository.save(note));
  }

  @Transactional
  public NoteResponse updateNote(String noteId, MemberResponse loginUser, NoteRequest noteRequest) {
    // 1. 노트 찾기
    Note note = noteRepository.findById(noteId)
        .orElseThrow(() -> new IllegalArgumentException("Note not found"));

    // 2. 접근 권한 확인
    if(!note.isPublicAccess() && !note.getAuthor().getId().equals(loginUser.getId())) {
      throw new CustomException(ErrorCode.NOTE_ACCESS_DENIED);
    }

    // 3. null이 아닌 필드만 업데이트
    Optional.ofNullable(noteRequest.getTitle()).ifPresent(note::setTitle);
    Optional.ofNullable(noteRequest.getContent()).ifPresent(note::setContent);
    Optional.of(noteRequest.isPublicAccess()).ifPresent(note::setPublicAccess);

    // 4. 저장 및 응답 반환
    return NoteResponse.of(noteRepository.save(note));
  }

  public void deleteNote(String noteId, MemberResponse loginUser) {
    Note note = noteRepository.findById(noteId)
        .orElseThrow(() -> new IllegalArgumentException("Note not found"));

    // 2. 접근 권한 확인
    if(!note.getAuthor().getId().equals(loginUser.getId())) {
      throw new CustomException(ErrorCode.NOTE_ACCESS_DENIED);
    }

    noteRepository.delete(note);
  }
}
