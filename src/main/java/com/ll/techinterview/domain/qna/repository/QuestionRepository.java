package com.ll.techinterview.domain.qna.repository;

import com.ll.techinterview.domain.qna.document.Question;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {

  // 참여자 ID로 질문 검색
  @Query("{'participants.id': ?0}")
  List<Question> findByParticipantId(Long participantId);

  List<Question> findBySpaceId(Long spaceId);
}