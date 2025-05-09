package com.ll.techinterview.domain.contest.dto.response;

import com.ll.techinterview.domain.contest.document.Problem;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProblemResponse {
  private Long id;
  private String problem;
  private String techClass;
  private String aiAnswer;
  private List<AnswerResponse> answers;

  public static ProblemResponse of(Problem problem) {
    return ProblemResponse.builder()
        .id(problem.getId())
        .problem(problem.getProblem())
        .techClass(problem.getTechClass())
        .aiAnswer(problem.getAiAnswer())
        .answers(problem.getAnswers().stream()
            .map(AnswerResponse::of)
            .toList())
        .build();
  }
}
