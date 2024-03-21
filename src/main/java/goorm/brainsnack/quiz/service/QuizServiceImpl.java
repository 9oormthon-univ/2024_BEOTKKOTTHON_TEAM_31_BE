package goorm.brainsnack.quiz.service;


import goorm.brainsnack.exception.BaseException;
import goorm.brainsnack.exception.ErrorCode;
import goorm.brainsnack.exception.QuizException;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.repository.MemberRepository;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import goorm.brainsnack.quiz.domain.QuizData;
import goorm.brainsnack.quiz.dto.QuizResponseDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto.CategoryQuizListDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto.SingleGradeDto;
import goorm.brainsnack.quiz.repository.MemberQuizRepository;
import goorm.brainsnack.quiz.repository.QuizDataRepository;
import goorm.brainsnack.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static goorm.brainsnack.quiz.dto.QuizRequestDto.*;
import static goorm.brainsnack.quiz.dto.QuizRequestDto.FullGradeRequestDto;
import static goorm.brainsnack.quiz.dto.QuizRequestDto.SingleGradeRequestDto;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.*;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.FullGradeDto;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.QuizDetailDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final MemberRepository memberRepository;
    private final MemberQuizRepository memberQuizRepository;
    private final QuizRepository quizRepository;
    private final QuizDataRepository dataRepository;

    @Override
    public QuizDetailDto findQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(ErrorCode.NOT_EXIST_QUIZ));
        // 여기서 DTO 로 반환해서 Controller 에게 넘겨주기
        return QuizDetailDto.from(quiz);
    }
    
    @Override
    public GetTotalMemberDto getTotalNum(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        List<MemberQuiz> members = memberQuizRepository.findAllByMember(member);

        // 풀었던 퀴즈가 없는 경우
        if (members.isEmpty()) {
            return GetTotalMemberDto.from(0);
        }

        int totalQuizNum = memberQuizRepository.findAllByMember(member).size();
        return GetTotalMemberDto.from(totalQuizNum);
    }

    @Override
    public CategoryQuizListDto getCategoryQuizList(String categoryName) {
        QuizCategory category = QuizCategory.getInstance(categoryName);

        List<Quiz> quizList = quizRepository.findAllByCategory(category);

        return CategoryQuizListDto.builder()
                .size(quizList.size())
                .quizDetailDtoList(quizList.stream()
                        .map(QuizDetailDto::from)
                        .toList())
                .build();
    }

    @Override
    public FullGradeDto gradeFullQuiz(Long memberId, String category, FullGradeRequestDto request) {
        return FullGradeDto.from(request.getGradeRequestList().stream()
                .map(r -> gradeSingleQuiz(memberId, r.getId(), r))
                .toList());
    }

    @Override
    @Transactional
    public SimilarQuizSingleGradeDto gradeSiringSimilarQuiz(Long memberId, SimilarQuizSingleGradeRequestDto request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));

        /**
         * quizNum 을 지정해주기 위해서 가져오는 코드
         */
        List<Quiz> quizList = quizRepository.findAllByCategory(QuizCategory.getInstance(request.getCategory()));

        // 유사 문제 채점했기 떄문에 이때 저장
        Quiz similarQuiz = Quiz.of((quizList.size()+1), request.getTitle(), request.getExample(),
                request.getChoiceFirst(), request.getChoiceSecond(), request.getChoiceThird(),
                request.getChoiceFourth(), request.getChoiceFifth(), request.getAnswer(), request.getSolution(),
                request.getIsSimilar(), QuizCategory.getInstance(request.getCategory()));
        quizRepository.save(similarQuiz);

        // MemberQuiz 에도 저장
        MemberQuiz memberQuiz = memberQuizRepository.save(MemberQuiz.toSimilarQuiz(request, member, similarQuiz));

        // 여기만 만들면 유사 문제 채점도 완료
        return SimilarQuizSingleGradeDto.of(similarQuiz,memberQuiz);
    }

    @Transactional
    @Override
    public SingleGradeDto gradeSingleQuiz(Long memberId, Long quizId, SingleGradeRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_QUIZ));

        MemberQuiz memberQuiz = memberQuizRepository.save(MemberQuiz.of(request, member, quiz));

        QuizData data = dataRepository.findByQuiz(quiz)
                .orElse(QuizData.from(quiz));
        data.updateQuizData(memberQuiz);

        int ratio = 0;
        if (data.getQuizParticipantsNum() != 0) {
            ratio = data.getCorrectAnswerNum() / data.getQuizParticipantsNum();
        }

        return SingleGradeDto.of(quiz, memberQuiz, data, ratio);
    }
}
