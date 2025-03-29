package com.ll.techinterview.domain.qna.dto;

import com.ll.techinterview.domain.qna.document.Question;
import com.ll.techinterview.domain.qna.document.Question.Participant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
  private String id;
  private Long spaceId;
  private String techClass;
  private String questionText;
  private Participant author;
  private List<Participant> participants;
  private Map<String, String> answers;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static QuestionResponse of(Question question) {
    return QuestionResponse.builder()
        .id(question.getId())
        .spaceId(question.getSpaceId())
        .techClass(question.getTechClass())
        .questionText(question.getQuestionText())
        .author(question.getAuthor())
        .participants(question.getParticipants())
        .answers(question.getAnswers())
        .createdAt(question.getCreatedAt())
        .updatedAt(question.getUpdatedAt())
        .build();
  }
}