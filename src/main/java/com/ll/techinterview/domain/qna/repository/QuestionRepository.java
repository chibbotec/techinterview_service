package com.ll.techinterview.domain.qna.repository;


import com.ll.techinterview.domain.qna.entity.Question;
import com.ll.techinterview.global.enums.TechClass;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

  List<Question> findBySpaceId(Long spaceId);

  Page<Question> findBySpaceId(Long spaceId, Pageable pageable);

  // Repository 메서드 수정
  @Query("SELECT q FROM Question q JOIN q.techInterview t WHERE q.spaceId = :spaceId AND t.techClass = :techClass")
  Page<Question> findBySpaceIdAndTechInterviewTechClass(
      @Param("spaceId") Long spaceId,
      @Param("techClass") TechClass techClass,
      Pageable pageable
  );

  @Query("SELECT q FROM Question q JOIN q.techInterview t WHERE q.spaceId = :spaceId " +
      "AND (:techClass IS NULL OR t.techClass = :techClass) " +
      "AND (:startDate IS NULL OR q.createdAt >= :startDate) " +
      "AND (:endDate IS NULL OR q.createdAt <= :endDate)")
  Page<Question> findBySearchCondition(
      @Param("spaceId") Long spaceId,
      @Param("techClass") TechClass techClass,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      Pageable pageable
  );
}