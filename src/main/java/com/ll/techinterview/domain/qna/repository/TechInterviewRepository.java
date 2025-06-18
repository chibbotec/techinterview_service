package com.ll.techinterview.domain.qna.repository;


import com.ll.techinterview.global.enums.InterviewType;
import com.ll.techinterview.global.enums.TechClass;
import com.ll.techinterview.global.jpa.TechInterview;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechInterviewRepository extends JpaRepository<TechInterview, Long> {

  // 기존 메서드를 Pageable 추가로 수정
  Page<TechInterview> findByInterviewType(InterviewType type, Pageable pageable);

  // 새로 추가
  Page<TechInterview> findByInterviewTypeAndTechClass(
      InterviewType type, TechClass techClass, Pageable pageable);

  List<TechInterview> findByTechClassIn(List<TechClass> techClasses);

}
