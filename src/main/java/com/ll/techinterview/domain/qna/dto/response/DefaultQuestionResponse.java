package com.ll.techinterview.domain.qna.dto.response;

import com.ll.techinterview.global.enums.TechClass;
import com.ll.techinterview.global.jpa.TechInterview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultQuestionResponse {
  private Long id;
  private TechClass techClass;
  private String aiAnswer;
  private String keyPoints;
  private String additionalTopics;
  private String questionText;

  public static DefaultQuestionResponse from(TechInterview techInterview) {
    return DefaultQuestionResponse.builder()
        .id(techInterview.getId())
        .techClass(techInterview.getTechClass())
        .aiAnswer(techInterview.getAiAnswer())
        .keyPoints(techInterview.getKeyPoints())
        .additionalTopics(techInterview.getAdditionalTopics())
        .questionText(techInterview.getQuestion())
        .build();
  }
}
