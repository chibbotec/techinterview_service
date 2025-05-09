package com.ll.techinterview.domain.contest.dto.response;

import com.ll.techinterview.domain.contest.document.Contest;
import com.ll.techinterview.domain.contest.document.Participant;
import com.ll.techinterview.domain.contest.service.Submit;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContestDetailResponse {

  private Long id;

  private String title;

  private LocalDateTime createdAt;

  private Long timeoutMillis;

  private Submit submit;

  private List<ParticipantResponse> participants;

  private List<ProblemResponse> problems;

  public static ContestDetailResponse of(Contest contest) {
    return ContestDetailResponse.builder()
        .id(contest.getId())
        .title(contest.getTitle())
        .submit(contest.getSubmit())
        .timeoutMillis(contest.getTimeoutMillis())
        .createdAt(contest.getCreatedAt())
        .participants(contest.getParticipants().stream()
            .map(ParticipantResponse::of)
            .toList())
        .problems(contest.getProblems().stream()
            .map(ProblemResponse::of)
            .toList())
        .build();
  }

  @Getter
  @Builder
  public static class ParticipantResponse {
    private Long id;
    private String nickname;
    private Submit submit;

    public static ParticipantResponse of(Participant participant) {
      return ParticipantResponse.builder()
          .id(participant.getMemberId())
          .nickname(participant.getNickname())
          .submit(participant.getSubmit())
          .build();
    }
  }
}