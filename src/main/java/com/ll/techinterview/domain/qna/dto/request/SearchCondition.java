package com.ll.techinterview.domain.qna.dto.request;

import com.ll.techinterview.global.enums.TechClass;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SearchCondition {
  private TechClass techClass;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
