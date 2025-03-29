package com.ll.techinterview.domain.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerRequest {
  private String questionId;
  private String answerText;
  private String techClass;
}
