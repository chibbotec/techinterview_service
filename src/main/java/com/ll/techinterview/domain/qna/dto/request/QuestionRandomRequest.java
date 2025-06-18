package com.ll.techinterview.domain.qna.dto.request;

import com.ll.techinterview.global.enums.TechClass;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRandomRequest {
  private List<TechClass> techClasses;
  private List<SpaceMemberRequest> participants;

  private Integer randomCount;
}