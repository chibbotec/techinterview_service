package com.ll.techinterview.global.client;

import com.ll.techinterview.global.techEnum.Submit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
  private Long id;
  private String username;
  private String email;
  private String nickname;
  private Submit submit;
}
