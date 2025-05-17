package com.ll.techinterview.domain.qna.entity;

import com.ll.techinterview.global.jpa.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
public class ParticipantQnA extends Member {

  @ManyToOne
  private Question question;


}