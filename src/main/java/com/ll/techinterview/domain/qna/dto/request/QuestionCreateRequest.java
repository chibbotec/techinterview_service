package com.ll.techinterview.domain.qna.dto.request;

import com.ll.techinterview.global.techEnum.TechClass;
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
  private TechClass techClass;
  private List<SpaceMemberRequest> participants;
}
