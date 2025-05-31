package com.ll.techinterview.domain.contest.dto.response;

import com.ll.techinterview.domain.contest.entity.Problem;
import com.ll.techinterview.global.enums.TechClass;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProblemResponse {
  private Long id;
  private String problem;
  private TechClass techClass;
  private String aiAnswer;
  private List<AnswerResponse> answers;

  public static ProblemResponse of(Problem problem) {
    return ProblemResponse.builder()
        .id(problem.getId())
        .problem(problem.getTechInterview().getQuestion())
        .techClass(problem.getTechInterview().getTechClass())
        .aiAnswer(problem.getTechInterview().getAiAnswer())
        .answers(problem.getAnswers().stream()
            .map(AnswerResponse::of)
            .toList())
        .build();
  }
}
