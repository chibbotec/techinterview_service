package com.ll.techinterview.domain.contest.dto.response;

import com.ll.techinterview.domain.contest.entity.Contest;
import com.ll.techinterview.global.enums.Submit;
import com.ll.techinterview.global.client.MemberResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContestSummaryResponse {

  private Long id;

  private String title;

  private LocalDateTime createdAt;

  private Submit submit;

  private List<MemberResponse> participants;

  public static ContestSummaryResponse of(Contest contest) {
    return ContestSummaryResponse.builder()
        .id(contest.getId())
        .title(contest.getTitle())
        .submit(contest.getSubmit())
        .createdAt(contest.getCreatedAt())
        .participants(contest.getParticipants().stream()
            .map(participant -> MemberResponse.builder()
                .id(participant.getMemberId())
                .nickname(participant.getNickname())
                .submit(participant.getSubmit())
                .build())
            .collect(Collectors.toList()))
        .build();
  }
}
