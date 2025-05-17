package com.ll.techinterview.domain.qna.repository;


import com.ll.techinterview.domain.qna.entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

  List<Question> findBySpaceId(Long spaceId);
}