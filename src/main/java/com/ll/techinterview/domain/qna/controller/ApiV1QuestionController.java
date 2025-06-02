package com.ll.techinterview.domain.qna.controller;

import com.ll.techinterview.domain.qna.dto.request.QuestionAnswerRequest;
import com.ll.techinterview.domain.qna.dto.request.QuestionCreateDbRequest;
import com.ll.techinterview.domain.qna.dto.request.QuestionCreateRequest;
import com.ll.techinterview.domain.qna.dto.request.SearchCondition;
import com.ll.techinterview.domain.qna.dto.response.DefaultQuestionResponse;
import com.ll.techinterview.domain.qna.dto.response.QuestionResponse;
import com.ll.techinterview.domain.qna.service.QuestionService;
import com.ll.techinterview.global.client.MemberResponse;
import com.ll.techinterview.global.enums.InterviewType;
import com.ll.techinterview.global.enums.TechClass;
import com.ll.techinterview.global.webMvc.LoginUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tech-interview/{spaceId}/questions")
@RequiredArgsConstructor
@Slf4j
public class ApiV1QuestionController {

  private final QuestionService questionService;

  @GetMapping("/db/{interviewType}")
  public ResponseEntity<Page<DefaultQuestionResponse>> getDefaultQuestionList(
      @PathVariable("interviewType") InterviewType interviewType,
      @RequestParam(value = "tech-class", required = false) TechClass techClass,
      @PageableDefault(size = 8) Pageable pageable
  ) {
    return ResponseEntity.ok(questionService.getQuestionListFromDB(interviewType, techClass, pageable));
  }

  @PostMapping("/db")
  public ResponseEntity<List<QuestionResponse>> createQuestionInDB(
      @LoginUser MemberResponse loginUser,
      @PathVariable("spaceId") Long spaceId,
      @RequestBody QuestionCreateDbRequest request) {
    return ResponseEntity.ok(questionService.createQuestionInDB(loginUser, spaceId, request));
  }

  @PostMapping
  public ResponseEntity<List<QuestionResponse>> createQuestion(
      @LoginUser MemberResponse loginUser,
      @PathVariable("spaceId") Long spaceId,
      @RequestBody QuestionCreateRequest request) {
    return ResponseEntity.ok(questionService.createQuestion(loginUser, spaceId, request));
  }

  @GetMapping
  public ResponseEntity<Page<QuestionResponse>> getQuestionList(
      @PathVariable("spaceId") Long spaceId,
      @RequestParam (value = "tech-class", required = false) TechClass techClass,
      @PageableDefault(
          size = 20,
          sort = "createdAt",
          direction = Direction.DESC) Pageable pageable
  ) {
    return ResponseEntity.ok(questionService.getQuestionList(spaceId, techClass, pageable));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteQuestion(
      @PathVariable("id") Long id) {
    questionService.deleteQuestion(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<QuestionResponse> getQuestion(
      @PathVariable("id") Long id) {
    return ResponseEntity.ok(questionService.getQuestion(id));
  }

  @PostMapping("/answers")
  public ResponseEntity<QuestionResponse> addAnswer(
      @LoginUser MemberResponse loginUser,
      @RequestBody QuestionAnswerRequest request) {

    return ResponseEntity.ok(questionService.addAnswer(loginUser, request));
  }

  @PostMapping("/search")
  public ResponseEntity<Page<QuestionResponse>> search(
      @PathVariable("spaceId") Long spaceId,
      @RequestBody SearchCondition searchCondition,
      @PageableDefault(
          size = 20,
          sort = "createdAt",
          direction = Direction.DESC) Pageable pageable
  ){
    return ResponseEntity.ok(questionService.searchQuestions(spaceId, searchCondition, pageable));
  }
}
