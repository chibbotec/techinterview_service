package com.ll.techinterview.domain.qna.entity;

import com.ll.techinterview.global.jpa.TechInterview;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private TechInterview techInterview;

  private Long spaceId;

  private Long authorId;

  private String authorNickname;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
  private List<ParticipantQnA> participants;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
  private List<Comment> comments = new ArrayList<>();
  
  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;


  public boolean isParticipant(Long participantId) {
    // participants 리스트에서 filter로 참여자 찾기
    if (participants != null) {
      return participants.stream()
          .filter(participant -> participant.getMemberId().equals(participantId))
          .findAny()
          .isPresent();
    }

    return false;
  }

  public void addComment(Comment comment) {
    this.comments.add(comment);
  }
}
