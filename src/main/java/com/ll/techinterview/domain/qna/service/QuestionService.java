package com.ll.techinterview.domain.qna.service;

import com.ll.techinterview.domain.qna.document.Question;
import com.ll.techinterview.domain.qna.dto.QuestionAnswerRequest;
import com.ll.techinterview.domain.qna.dto.QuestionCreateRequest;
import com.ll.techinterview.domain.qna.dto.QuestionResponse;
import com.ll.techinterview.domain.qna.dto.SpaceMemberRequest;
import com.ll.techinterview.domain.qna.repository.QuestionRepository;
import com.ll.techinterview.global.client.MemberResponse;
import com.ll.techinterview.global.error.ErrorCode;
import com.ll.techinterview.global.exception.CustomException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

  private final QuestionRepository questionRepository;

  @Transactional
  public QuestionResponse createQuestion(MemberResponse loginUser, Long spaceId, QuestionCreateRequest request) {
    Question question = Question.builder()
        .spaceId(spaceId)
        .techClass(request.getTechClass())
        .questionText(request.getQuestionText())
        .build();

    // 질문자 추가
    question.addAuthor(loginUser.getId(), loginUser.getNickname());

    // 참여자 추가
    for (SpaceMemberRequest participant : request.getParticipants()) {
      Long id = participant.getId();
      String nickname = participant.getNickname();
      question.addParticipant(id, nickname);
    }

    return QuestionResponse.of(questionRepository.save(question));
  }

  public List<QuestionResponse> getQuestionList(Long spaceId) {
    List<Question> questions = questionRepository.findBySpaceId(spaceId);
    return questions.stream().map(QuestionResponse::of).toList();
  }

  public QuestionResponse getQuestion(String id) {

    Question question = questionRepository.findById(id)
        .orElseThrow(()->new CustomException(ErrorCode.TECH_INTERVIEW_NOT_FOUND));

    return QuestionResponse.of(question);
  }

  @Transactional
  public QuestionResponse addAnswer(MemberResponse loginUser, QuestionAnswerRequest request) {
    Optional<Question> optionalQuestion = questionRepository.findById(request.getQuestionId());

    if (optionalQuestion.isPresent()) {
      Question question = optionalQuestion.get();

      // 참여자인지 확인
      if (question.findParticipantById(loginUser.getId()).isEmpty()) {
        throw new CustomException(ErrorCode.NOT_PARTICIPANT);
      }

      String answerText = request.getAnswerText();

      question.addAnswer(loginUser.getId(), answerText);
      return QuestionResponse.of(questionRepository.save(question));
    }

    throw new CustomException(ErrorCode.TECH_INTERVIEW_NOT_FOUND);
  }

  // 참여자 추가
  @Transactional
  public List<QuestionResponse> addParticipant(Long spaceId, MemberResponse request) {

    List<Question> questions = questionRepository.findBySpaceId(spaceId);

    if (questions.isEmpty()) {
      throw new CustomException(ErrorCode.TECH_INTERVIEW_NOT_FOUND);
    }

    for (Question question : questions) {
      question.addParticipant(request.getId(), request.getNickname());
      questionRepository.save(question);
    }

    return questions.stream().map(QuestionResponse::of).toList();
  }

  // 참여자 제거
  @Transactional
  public List<QuestionResponse> removeParticipant(Long spaceId, MemberResponse request) {
    List<Question> questions = questionRepository.findBySpaceId(spaceId);

    if (questions.isEmpty()) {
      throw new CustomException(ErrorCode.TECH_INTERVIEW_NOT_FOUND);
    }

    for (Question question : questions) {
      question.removeParticipant(request.getId());
      questionRepository.save(question);
    }

    return questions.stream().map(QuestionResponse::of).toList();
  }

  @Transactional
  public void deleteQuestion(String id) {
    Question question = questionRepository.findById(id)
        .orElseThrow(()->new CustomException(ErrorCode.TECH_INTERVIEW_NOT_FOUND));
    questionRepository.delete(question);
  }
}
