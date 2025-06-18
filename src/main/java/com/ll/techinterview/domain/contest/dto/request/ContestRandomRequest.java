package com.ll.techinterview.domain.contest.dto.request;

import com.ll.techinterview.global.client.MemberResponse;
import com.ll.techinterview.global.enums.TechClass;
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
public class ContestRandomRequest {

  private String title;

  private LocalDateTime createAt;

  private Long timeoutMillis;

  private List<MemberResponse> participants;

  private Integer randomCount;

  private List<TechClass> techClasses;
}
