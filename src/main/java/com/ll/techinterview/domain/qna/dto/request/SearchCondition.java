package com.ll.techinterview.domain.qna.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SearchCondition {
  private String techClass;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
