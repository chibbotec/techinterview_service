package com.ll.techinterview.domain.contest.controller;

import com.ll.techinterview.domain.contest.dto.request.ContestCreateRequest;
import com.ll.techinterview.domain.contest.dto.request.ContestSubmitRequest;
import com.ll.techinterview.domain.contest.dto.response.ContestDetailResponse;
import com.ll.techinterview.domain.contest.dto.response.ContestSummaryResponse;
import com.ll.techinterview.domain.contest.service.ContestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tech-interview/{spaceId}/contests")
@RequiredArgsConstructor
@Slf4j
public class ApiV1ContestController {

  private final ContestService contestService;

  @GetMapping
  public ResponseEntity<List<ContestSummaryResponse>> getContestList(
      @PathVariable("spaceId") Long spaceId
  ){
    return ResponseEntity.ok(contestService.getContestList(spaceId));
  }

  @GetMapping("/{contestId}")
  public ResponseEntity<ContestDetailResponse> getContest(
      @PathVariable("contestId") Long contestId){
    return ResponseEntity.ok(contestService.getContest(contestId));
  }

  @PostMapping("/{contestId}/submit")
  public ResponseEntity sumbitTest(
      @PathVariable("contestId") Long contestId,
      @RequestBody ContestSubmitRequest contestSubmitRequest
  ){
    contestService.submitTest(contestId, contestSubmitRequest);
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity createContest(
      @PathVariable("spaceId") Long spaceId,
      @RequestBody ContestCreateRequest contestCreateRequest
  ){
    contestService.createContest(spaceId, contestCreateRequest);
    return ResponseEntity.ok().build();
  }
}
