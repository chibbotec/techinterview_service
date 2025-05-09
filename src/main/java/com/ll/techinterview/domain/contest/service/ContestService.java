package com.ll.techinterview.domain.contest.service;

import com.ll.techinterview.domain.contest.document.Answer;
import com.ll.techinterview.domain.contest.document.Contest;
import com.ll.techinterview.domain.contest.document.Participant;
import com.ll.techinterview.domain.contest.document.Problem;
import com.ll.techinterview.domain.contest.dto.request.ContestCreateRequest;
import com.ll.techinterview.domain.contest.dto.request.ContestSubmitRequest;
import com.ll.techinterview.domain.contest.dto.response.ContestDetailResponse;
import com.ll.techinterview.domain.contest.dto.response.ContestSummaryResponse;
import com.ll.techinterview.domain.contest.repository.ContestRepository;
import com.ll.techinterview.global.error.ErrorCode;
import com.ll.techinterview.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContestService {

  private final ContestRepository contestRepository;

  public List<ContestSummaryResponse> getContestList(Long spaceId) {

    List<Contest> contests = contestRepository.findAllBySpaceId(spaceId);

    return contests.stream()
        .map(ContestSummaryResponse::of)
        .toList();
  }

  public ContestDetailResponse getContest(Long contestId) {
    Contest contest = contestRepository.findById(contestId).orElseThrow(
        () -> new CustomException(ErrorCode.CONTEST_NOT_FOUND)
    );

    return ContestDetailResponse.of(contest);
  }

  @Transactional
  public void createContest(Long spaceId, ContestCreateRequest contestCreateRequest) {
    // Contest 엔티티 생성
    Contest contest = Contest.builder()
        .spaceId(spaceId)
        .title(contestCreateRequest.getTitle())
        .timeoutMillis(contestCreateRequest.getTimeoutMillis())
        .submit(Submit.IN_PROGRESS)
        .build();

    // 1. 참가자(Participant) 엔티티 생성
    List<Participant> participants = contestCreateRequest.getParticipants().stream()
        .map(memberResponse -> {
          Participant participant = Participant.builder()
              .memberId(memberResponse.getId())
              .nickname(memberResponse.getNickname())
              .submit(Submit.IN_PROGRESS)
              .contest(contest) // Contest 설정
              .build();
          return participant;
        })
        .toList();

    // 2. 문제(Problem) 엔티티 생성
    List<Problem> problems = contestCreateRequest.getProblems().stream()
        .map(problemContent -> {
          Problem problem = Problem.builder()
              .problem(problemContent.getQuestionText())
              .techClass(problemContent.getTechClass())
              .aiAnswer(problemContent.getAiAnswer())
              .answers(List.of())
              .contest(contest) // Contest 설정
              .build();
          return problem;
        })
        .toList();

    // 참가자와 문제를 Contest에 설정
    contest.setParticipants(participants);
    contest.setProblems(problems);

    // 저장
    Contest savedContest = contestRepository.save(contest);
  }

  @Transactional
  public void submitTest(Long contestId, ContestSubmitRequest contestSubmitRequest) {
    Contest contest = contestRepository.findById(contestId).orElseThrow(() ->
        new CustomException(ErrorCode.CONTEST_NOT_FOUND)
    );

    // memberId로 참가자 찾기
    Participant participant = contest.getParticipants().stream()
        .filter(p -> p.getMemberId().equals(contestSubmitRequest.getMemberId()))
        .findFirst()
        .orElseThrow(() ->
            new CustomException(ErrorCode.PARTICIPANT_NOT_FOUND)
        );

    // 참가자 상태를 제출 완료로 변경
    participant.setSubmit(Submit.COMPLETED);

    // 각 문제별 답변 처리
    contestSubmitRequest.getAnswers().forEach(answerRequest -> {
      // 문제 ID로 문제 찾기
      Problem problem = contest.getProblems().stream()
          .filter(p -> p.getId().equals(answerRequest.getProblemId()))
          .findFirst()
          .orElseThrow(() ->
              new CustomException(ErrorCode.PROBLEM_NOT_FOUND)
          );

      // 답변 생성 및 저장
      Answer answer = Answer.builder()
          .answer(answerRequest.getAnswer())
          .problem(problem)
          .participant(participant)
          .build();

      // 문제의 답변 목록에 추가
      problem.getAnswers().add(answer);
    });

    // 모든 참가자가 제출했는지 확인
    boolean allSubmitted = contest.getParticipants().stream()
        .allMatch(p -> p.getSubmit() == Submit.COMPLETED);

    // 모든 참가자가 제출했다면 컨테스트 상태 업데이트
    if (allSubmitted) {
      contest.setSubmit(Submit.COMPLETED);
    }
  }
}
