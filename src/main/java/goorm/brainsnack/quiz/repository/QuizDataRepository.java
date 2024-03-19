package goorm.brainsnack.quiz.repository;

import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizDataRepository extends JpaRepository<QuizData, Long> {

    Optional<QuizData> findByQuiz(Quiz quiz);
}
