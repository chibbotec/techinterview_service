package com.ll.techinterview.domain.qna.dto.request;

import com.ll.techinterview.global.enums.InterviewType;
import com.ll.techinterview.global.enums.TechClass;
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
public class QuestionCreateRequest {
  private List<Questions> questions;
  private List<SpaceMemberRequest> participants;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Questions {
    private String questionText;
    private TechClass techClass;
    private InterviewType interviewType;
  }
}
