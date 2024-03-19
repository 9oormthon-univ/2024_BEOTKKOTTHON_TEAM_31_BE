package goorm.brainsnack.quiz.service;

import goorm.brainsnack.quiz.dto.QuizRequestDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto.GetTotalMemberDto;

import static goorm.brainsnack.quiz.dto.QuizResponseDto.CategoryQuizListDto;

public interface QuizService {
  
    GetTotalMemberDto getTotalNum(Long memberId);
    CategoryQuizListDto getCategoryQuizList(String category);

    QuizResponseDto.SingleGradeQuizDto gradeSingleQuiz(Long memberId, Long quizId, QuizRequestDto.SingleGradeRequestDto request);
}
