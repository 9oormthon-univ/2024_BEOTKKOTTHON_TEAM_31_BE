package goorm.brainsnack.quiz.service;

import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.dto.QuizResponseDto;

import java.util.Optional;

public interface QuizService {

    QuizResponseDto.QuizDto findQuiz(Long quizId);
}
