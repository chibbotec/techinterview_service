package com.ll.techinterview.domain.contest.dto.request;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestSubmitRequest {
  private Long memberId;

  private List<AnswerRequest> answers;

  @Setter
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AnswerRequest {
    private Long problemId;
    private String answer;
  }
}
