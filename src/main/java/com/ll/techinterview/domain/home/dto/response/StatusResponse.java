package com.ll.techinterview.domain.home.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {
  long totalContestCount;
  long totalQuestionCount;
  long totalNoteCount;
}
