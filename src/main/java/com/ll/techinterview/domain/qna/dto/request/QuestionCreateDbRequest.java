package com.ll.techinterview.domain.qna.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateDbRequest {
  List<Long> questionIds;
  private List<SpaceMemberRequest> participants;
}
