package com.ll.techinterview.domain.qna.repository;


import com.ll.techinterview.global.enums.InterviewType;
import com.ll.techinterview.global.jpa.TechInterview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechInterviewRepository extends JpaRepository<TechInterview, Long> {

  List<TechInterview> findByInterviewType(InterviewType type);

}
