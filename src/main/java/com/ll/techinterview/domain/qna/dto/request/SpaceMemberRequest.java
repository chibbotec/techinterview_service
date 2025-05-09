package com.ll.techinterview.domain.qna.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceMemberRequest {
  private Long id;
  private String nickname;
}
