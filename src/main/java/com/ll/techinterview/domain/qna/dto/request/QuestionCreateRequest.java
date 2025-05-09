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
public class QuestionCreateRequest {
  private String questionText;
  private String techClass;
  private List<SpaceMemberRequest> participants;
}
