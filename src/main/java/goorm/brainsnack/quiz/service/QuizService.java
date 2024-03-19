package goorm.brainsnack.quiz.service;

import goorm.brainsnack.quiz.dto.QuizResponseDto;

import static goorm.brainsnack.quiz.dto.QuizResponseDto.CategoryQuizListDto;

public interface QuizService {
  
    QuizResponseDto.GetTotalMemberDto getTotalNum(Long memberId);
    CategoryQuizListDto getCategoryQuizList(String category);
  
}
