package com.ll.techinterview.global.jpa;

import com.ll.techinterview.domain.contest.entity.Problem;
import com.ll.techinterview.domain.qna.entity.Question;
import com.ll.techinterview.global.enums.InterviewType;
import com.ll.techinterview.global.enums.TechClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode
public class TechInterview {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private TechClass techClass;

  private String question;

  @Column(columnDefinition = "TEXT")
  private String aiAnswer;

  @Column(name = "key_point", columnDefinition = "TEXT")
  private String keyPoints;

  @Column(name = "additional_topics", columnDefinition = "TEXT")
  private String additionalTopics;

  @Column(name = "type")
  private InterviewType interviewType;

  @OneToMany(mappedBy = "techInterview")
  private List<Problem> problems = new ArrayList<>();

  @OneToMany(mappedBy = "techInterview")
  private List<Question> questions = new ArrayList<>();

  // 편의 메서드 추가
  public void addProblem(Problem problem) {
    this.problems.add(problem);
    problem.setTechInterview(this);
  }

  public void addQuestion(Question question) {
    this.questions.add(question);
    question.setTechInterview(this);
  }
}
