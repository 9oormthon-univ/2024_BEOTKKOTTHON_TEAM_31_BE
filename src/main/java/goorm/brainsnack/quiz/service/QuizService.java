package goorm.brainsnack.quiz.service;

import static goorm.brainsnack.quiz.dto.QuizResponseDto.CategoryQuizListDto;

public interface QuizService {
    CategoryQuizListDto getCategoryQuizList(String category);
}
