package com.ll.techinterview.domain.qna.document;

import jakarta.persistence.EntityListeners;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "questions")
public class Question {
  @Id
  private String id;

  private Long spaceId;

  private String techClass;

  private String questionText;

  private Participant author;

  @Builder.Default
  private List<Participant> participants = new ArrayList<>();

  @Builder.Default
  private Map<String, String> answers = new HashMap<>();

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  // 참여자 정보 내장 클래스
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @EntityListeners(AuditingEntityListener.class)
  public static class Participant {
    private Long id;
    private String nickname;
  }

  // 참여자 찾기
  public Optional<Participant> findParticipantById(Long id) {
    return participants.stream()
        .filter(p -> p.getId().equals(id))
        .findFirst();
  }

  // 질문자 추가
  public void addAuthor(Long id, String nickname) {
    author = Participant.builder()
        .id(id)
        .nickname(nickname)
        .build();
  }

  // 참여자 추가
  public void addParticipant(Long id, String nickname) {
    if (participants.stream().noneMatch(p -> p.getId().equals(id))) {
      participants.add(
          Participant.builder()
              .id(id)
              .nickname(nickname)
              .build()
      );
    }
  }

  // 참여자 제거
  public void removeParticipant(Long id) {
    participants.removeIf(p -> p.getId().equals(id));
    answers.remove("memberId:" + id);
  }

  // 답변 추가
  public void addAnswer(Long memberId, String answerText) {
    answers.put("memberId:" + memberId, answerText);
  }

  // AI 답변 추가
  public void addAIAnswer(String aiAnswer) {
    answers.put("ai", aiAnswer);
  }

  // AI 답변 조회
  public String getAIAnswer() {
    return answers.get("ai");
  }

  // 멤버 답변 조회
  public String getAnswerByMemberId(Long memberId) {
    return answers.get("memberId:" + memberId);
  }
}