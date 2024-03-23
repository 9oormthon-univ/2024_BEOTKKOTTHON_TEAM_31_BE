package goorm.brainsnack.quiz.repository;

import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import goorm.brainsnack.quiz.domain.SimilarQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimilarQuizRepository extends JpaRepository<SimilarQuiz, Long> {

    List<SimilarQuiz> findAllByCategory(QuizCategory category);
}
