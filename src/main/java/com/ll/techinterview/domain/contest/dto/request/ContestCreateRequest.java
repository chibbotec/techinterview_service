package com.ll.techinterview.domain.contest.dto.request;

import com.ll.techinterview.global.client.MemberResponse;
import java.time.LocalDateTime;
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
public class ContestCreateRequest {

  private String title;

  private LocalDateTime createAt;

  private Long timeoutMillis;

  private List<ProblemRequest> problems;

  private List<MemberResponse> participants;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProblemRequest{
    private String techClass;
    private String questionText;
    private String aiAnswer;
  }
}
