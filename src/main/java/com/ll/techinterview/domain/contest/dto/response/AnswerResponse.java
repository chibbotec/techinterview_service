package com.ll.techinterview.domain.contest.dto.response;

import com.ll.techinterview.domain.contest.document.Answer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class AnswerResponse {
  private Long memberId;

  private String nickname;

  private String answer;

  private int rank;

  private String feedback;

  public static AnswerResponse of(Answer answer) {
    return AnswerResponse.builder()
        .memberId(answer.getParticipant().getMemberId())
        .nickname(answer.getParticipant().getNickname())
        .answer(answer.getAnswer())
        .rank(answer.getRankScore())
        .feedback(answer.getFeedback())
        .build();
  }
}
