package com.ll.techinterview.domain.home.service;

import com.ll.techinterview.domain.contest.document.Contest;
import com.ll.techinterview.domain.contest.repository.ContestRepository;
import com.ll.techinterview.domain.home.dto.response.StatusResponse;
import com.ll.techinterview.domain.note.document.Note;
import com.ll.techinterview.domain.note.repository.NoteRepository;
import com.ll.techinterview.domain.qna.document.Question;
import com.ll.techinterview.domain.qna.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {

  private final ContestRepository contestRepository;
  private final QuestionRepository questionRepository;
  private final NoteRepository noteRepository;

  public StatusResponse getStatus(Long spaceId) {
    List<Contest> contests = contestRepository.findAllBySpaceId(spaceId);
    long totalContestCount = contests.size();

    List<Question> questions = questionRepository.findBySpaceId(spaceId);
    long totalQuestionCount = questions.size();

    List<Note> notes = noteRepository.findBySpaceId(spaceId);
    long totalNoteCount = notes.size();

    return StatusResponse.builder()
        .totalContestCount(totalContestCount)
        .totalQuestionCount(totalQuestionCount)
        .totalNoteCount(totalNoteCount)
        .build();

  }
}
