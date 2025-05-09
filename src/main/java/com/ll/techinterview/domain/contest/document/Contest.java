package com.ll.techinterview.domain.contest.document;

import com.ll.techinterview.domain.contest.service.Submit;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Contest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long spaceId;

  private String title;

  private Long timeoutMillis;

  @CreatedDate
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL)
  private List<Participant> participants;

  @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL)
  private List<Problem> problems;

  private Submit submit;
}
