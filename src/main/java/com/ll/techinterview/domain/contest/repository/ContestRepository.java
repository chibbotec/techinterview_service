package com.ll.techinterview.domain.contest.repository;

import com.ll.techinterview.domain.contest.entity.Contest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

  List<Contest> findAllBySpaceId(Long spaceId);
}
