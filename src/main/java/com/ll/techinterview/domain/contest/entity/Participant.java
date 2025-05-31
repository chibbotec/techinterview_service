package com.ll.techinterview.domain.contest.entity;

import com.ll.techinterview.global.enums.Submit;
import com.ll.techinterview.global.jpa.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Participant extends Member {
  private Submit submit;

  @ManyToOne(fetch = FetchType.LAZY)
  private Contest contest;

  @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
  private List<Answer> answers;
}
