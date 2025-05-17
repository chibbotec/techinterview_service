package com.ll.techinterview.domain.qna.dto.response;


import com.ll.techinterview.domain.qna.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
  private Long id;
  private Long memberId;
  private String nickname;
  private String comment;

  public static CommentResponse of(Comment comment) {
    return CommentResponse.builder()
        .id(comment.getId())
        .memberId(comment.getParticipantQna().getMemberId())
        .nickname(comment.getParticipantQna().getNickname())
        .comment(comment.getComment())
        .build();
  }
}
