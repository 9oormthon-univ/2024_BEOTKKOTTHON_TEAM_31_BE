package goorm.brainsnack.quiz.service;


import goorm.brainsnack.exception.ErrorCode;
import goorm.brainsnack.exception.QuizException;
import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.dto.QuizResponseDto;

import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import goorm.brainsnack.quiz.dto.QuizResponseDto.CategoryQuizListDto;
import goorm.brainsnack.exception.BaseException;
import goorm.brainsnack.exception.ErrorCode;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.repository.MemberRepository;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import goorm.brainsnack.quiz.dto.QuizResponseDto;
import goorm.brainsnack.quiz.repository.MemberQuizRepository;

import goorm.brainsnack.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static goorm.brainsnack.quiz.dto.QuizResponseDto.QuizDetailDto;


import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final MemberRepository memberRepository;
    private final MemberQuizRepository memberQuizRepository;
    private final QuizRepository quizRepository;


    @Override
    public QuizResponseDto.QuizDto findQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(ErrorCode.NOT_EXIST_QUIZ));
        // 여기서 DTO 로 반환해서 Controller 에게 넘겨주기
        QuizResponseDto.QuizDto quizDto = Quiz.toQuizDto(quiz);
        return quizDto;
    }

    
    @Override
    public QuizResponseDto.GetTotalMemberDto getTotalNum(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        List<MemberQuiz> members = memberQuizRepository.findAllByMember(member);

        // 풀었던 퀴즈가 없는 경우
        if (members.isEmpty()) {
            return QuizResponseDto.GetTotalMemberDto.from(0);
        }

        int totalQuizNum = memberQuizRepository.findAllByMember(member).size();
        return QuizResponseDto.GetTotalMemberDto.from(totalQuizNum);
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
      
}
