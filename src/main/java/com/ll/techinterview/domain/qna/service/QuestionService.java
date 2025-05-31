package com.ll.techinterview.domain.qna.service;


import com.ll.techinterview.domain.qna.dto.request.QuestionAnswerRequest;
import com.ll.techinterview.domain.qna.dto.request.QuestionCreateDbRequest;
import com.ll.techinterview.domain.qna.dto.request.QuestionCreateRequest;
import com.ll.techinterview.domain.qna.dto.request.QuestionCreateRequest.Questions;
import com.ll.techinterview.domain.qna.dto.request.SearchCondition;
import com.ll.techinterview.domain.qna.dto.response.DefaultQuestionResponse;
import com.ll.techinterview.domain.qna.dto.response.QuestionResponse;
import com.ll.techinterview.domain.qna.entity.Comment;
import com.ll.techinterview.domain.qna.entity.ParticipantQnA;
import com.ll.techinterview.domain.qna.entity.Question;
import com.ll.techinterview.domain.qna.repository.QuestionRepository;
import com.ll.techinterview.domain.qna.repository.TechInterviewRepository;
import com.ll.techinterview.global.client.MemberResponse;
import com.ll.techinterview.global.enums.InterviewType;
import com.ll.techinterview.global.enums.TechClass;
import com.ll.techinterview.global.error.ErrorCode;
import com.ll.techinterview.global.exception.CustomException;
import com.ll.techinterview.global.jpa.TechInterview;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

  private final QuestionRepository questionRepository;
  private final TechInterviewRepository techInterviewRepository;
  @PersistenceContext  // 추가: EntityManager 주입
  private EntityManager entityManager;

  @Transactional
  public List<QuestionResponse> createQuestionInDB(MemberResponse loginUser, Long spaceId,
      QuestionCreateDbRequest request) {
    List<TechInterview> techInterviews = techInterviewRepository.findAllById(
        request.getQuestionIds());

    if (techInterviews.isEmpty()) {
      throw new CustomException(ErrorCode.TECH_INTERVIEW_NOT_FOUND);
    }

    List<Question> questions = techInterviews.stream().map(
        techInterview -> {
          Question question = Question.builder()
              .spaceId(spaceId)
              .authorId(loginUser.getId())
              .authorNickname(loginUser.getNickname())
              .techInterview(techInterview)
              .comments(new ArrayList<>())
              .participants(new ArrayList<>())
              .build();

          List<ParticipantQnA> participants = request.getParticipants().stream().map(
              participantRequest -> {
                ParticipantQnA participant = ParticipantQnA.builder()
                    .memberId(participantRequest.getId())
                    .nickname(participantRequest.getNickname())
                    .build();
                participant.setQuestion(question); // 양방향 관계의 주인쪽 설정
                return participant;
              }
          ).collect(Collectors.toList());

          // 이 부분이 빠져있었습니다!
          question.setParticipants(participants);

          return question; // 이것도 빠져있었습니다!
        }
    ).collect(Collectors.toList());

    List<Question> savedQuestions = questionRepository.saveAll(questions);
    return savedQuestions.stream().map(QuestionResponse::of).collect(Collectors.toList());
  }

  @Transactional
  public List<QuestionResponse> createQuestion(MemberResponse loginUser, Long spaceId,
      QuestionCreateRequest request) {

    List<Question> savedQuestions = new ArrayList<>();

    for (Questions question : request.getQuestions()) {
      // 1. TechInterview 객체 생성 및 저장
      TechInterview techInterview = TechInterview.builder()
          .techClass(question.getTechClass())
          .question(question.getQuestionText())
          .interviewType(question.getInterviewType())
          .build();

      TechInterview savedTechInterview = techInterviewRepository.save(techInterview);

      // 2. Question 객체 생성
      Question newQuestion = Question.builder()
          .spaceId(spaceId)
          .authorId(loginUser.getId())
          .authorNickname(loginUser.getNickname())
          .techInterview(savedTechInterview)  // 중요: 저장된 TechInterview 설정
          .comments(new ArrayList<>())
          .participants(new ArrayList<>())
          .build();

      // 3. ParticipantQnA 객체 생성 및 양방향 관계 설정
      List<ParticipantQnA> participants = request.getParticipants().stream().map(
          participantRequest -> {
            ParticipantQnA participant = ParticipantQnA.builder()
                .memberId(participantRequest.getId())
                .nickname(participantRequest.getNickname())
                .build();
            participant.setQuestion(newQuestion); // 양방향 관계의 주인쪽 설정
            return participant;
          }
      ).collect(Collectors.toList());

      // 4. Question에 participants 설정
      newQuestion.setParticipants(participants);

      savedQuestions.add(newQuestion);
    }

    // 5. Question 저장 및 반환
    List<Question> savedQuestions2 = questionRepository.saveAll(savedQuestions);
    return savedQuestions2.stream().map(QuestionResponse::of).collect(Collectors.toList());
  }


  public List<QuestionResponse> getQuestionList(Long spaceId) {
    List<Question> questions = questionRepository.findBySpaceId(spaceId);
    return questions.stream().map(QuestionResponse::of).toList();
  }

  public QuestionResponse getQuestion(Long id) {

    Question question = questionRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.TECH_INTERVIEW_NOT_FOUND));

    return QuestionResponse.of(question);
  }

  @Transactional
  public QuestionResponse addAnswer(MemberResponse loginUser, QuestionAnswerRequest request) {
    Optional<Question> optionalQuestion = questionRepository.findById(request.getQuestionId());

    if (optionalQuestion.isPresent()) {
      Question question = optionalQuestion.get();

      // 참여자인지 확인
      if (!question.isParticipant(loginUser.getId())) {
        throw new CustomException(ErrorCode.NOT_PARTICIPANT);
      }

      String answerText = request.getAnswerText();

      // 참여자 정보 가져오기 - 참여자 ID로 ParticipantQnA 찾기
      ParticipantQnA participant = findParticipantByMemberId(question, loginUser.getId());

      // 새 Comment 객체 생성
      Comment comment = Comment.builder()
          .question(question)
          .participantQna(participant)
          .comment(answerText)
          .build();

      question.addComment(comment);

      return QuestionResponse.of(questionRepository.save(question));
    }

    throw new CustomException(ErrorCode.TECH_INTERVIEW_NOT_FOUND);
  }

  @Transactional
  public void deleteQuestion(Long id) {
    Question question = questionRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.TECH_INTERVIEW_NOT_FOUND));
    questionRepository.delete(question);
  }

  public List<QuestionResponse> searchQuestions(Long spaceId, SearchCondition searchCondition) {
    // 기본 검색 조건: spaceId 일치
    StringBuilder jpql = new StringBuilder(
        "SELECT q FROM Question q JOIN q.techInterview t WHERE q.spaceId = :spaceId");

    // 파라미터 맵 생성
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("spaceId", spaceId);

    // TechClass 조건 추가
    if (searchCondition.getTechClass() != null && !searchCondition.getTechClass().isEmpty()) {
      jpql.append(" AND t.techClass = :techClass");
      try {
        // String을 TechClass enum으로 변환
        TechClass techClass = TechClass.valueOf(searchCondition.getTechClass());
        parameters.put("techClass", techClass);
      } catch (IllegalArgumentException e) {
        // 유효하지 않은 TechClass인 경우 빈 결과 반환
        return Collections.emptyList();
      }
    }

    // 시작 날짜 조건 추가
    if (searchCondition.getStartDate() != null) {
      jpql.append(" AND q.createdAt >= :startDate");
      parameters.put("startDate", searchCondition.getStartDate());
    }

    // 종료 날짜 조건 추가
    if (searchCondition.getEndDate() != null) {
      jpql.append(" AND q.createdAt <= :endDate");
      parameters.put("endDate", searchCondition.getEndDate());
    }

    // 정렬 추가 (최신순)
    jpql.append(" ORDER BY q.createdAt DESC");

    // 쿼리 생성 및 파라미터 설정
    TypedQuery<Question> query = entityManager.createQuery(jpql.toString(), Question.class);
    parameters.forEach(query::setParameter);

    // 쿼리 실행 및 결과 변환
    List<Question> questions = query.getResultList();
    return questions.stream().map(QuestionResponse::of).toList();
  }

  private ParticipantQnA findParticipantByMemberId(Question question, Long memberId) {
    // 참여자 리스트에서 찾기
    if (question.getParticipants() != null) {
      return question.getParticipants().stream()
          .filter(p -> p.getMemberId().equals(memberId))
          .findFirst()
          .orElseThrow(() -> new CustomException(ErrorCode.NOT_PARTICIPANT));
    }

    throw new CustomException(ErrorCode.NOT_PARTICIPANT);
  }

  public List<DefaultQuestionResponse> getQuestionListFromDB(InterviewType interviewType) {
    List<TechInterview> defaultQuestions = techInterviewRepository.findByInterviewType(interviewType);

    return defaultQuestions.stream().map(
        techInterview -> (DefaultQuestionResponse.from(techInterview))
    ).toList();
  }


}
