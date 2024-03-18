package goorm.brainsnack.quiz.service;

import goorm.brainsnack.quiz.domain.Quiz;

import java.util.Optional;

public interface QuizService {

    Quiz findQuiz(Long quizId);
}
