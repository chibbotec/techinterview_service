package com.ll.techinterview.domain.qna.dto.response;

import com.ll.techinterview.domain.qna.entity.ParticipantQnA;
import com.ll.techinterview.domain.qna.entity.Question;
import com.ll.techinterview.global.enums.TechClass;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
  private Long id;
  private Long spaceId;
  private Long techInterviewId;
  private TechClass techClass;
  private String aiAnswer;
  private String keyPoints;
  private String additionalTopics;
  private String questionText;
  private ParticipantResponse author;
  private List<ParticipantResponse> participants;
  private List<CommentResponse> answers;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static QuestionResponse of(Question question) {
    return QuestionResponse.builder()
        .id(question.getId())
        .spaceId(question.getSpaceId())
        .techInterviewId(question.getTechInterview().getId())
        .techClass(question.getTechInterview().getTechClass())
        .questionText(question.getTechInterview().getQuestion())
        .aiAnswer(question.getTechInterview().getAiAnswer())
        .keyPoints(question.getTechInterview().getKeyPoints())
        .additionalTopics(question.getTechInterview().getAdditionalTopics())
        .author(ParticipantResponse.of(
            ParticipantQnA.builder()
                .memberId(question.getAuthorId())
                .nickname(question.getAuthorNickname())
                .build()
        ))
        .participants(question.getParticipants().stream().map(
            ParticipantResponse::of).toList(
        ))
        .answers(question.getComments().stream().map(
            CommentResponse::of).toList(
        ))
        .createdAt(question.getCreatedAt())
        .updatedAt(question.getUpdatedAt())
        .build();
  }

  @Getter
  @Builder
  public static class ParticipantResponse {
    private Long id;
    private String nickname;

    public static ParticipantResponse of(ParticipantQnA participant) {
      return ParticipantResponse.builder()
          .id(participant.getMemberId())
          .nickname(participant.getNickname())
          .build();
    }
  }
}