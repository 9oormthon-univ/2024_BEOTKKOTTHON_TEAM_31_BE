package goorm.brainsnack.quiz.repository;

import goorm.brainsnack.quiz.domain.SimilarQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimilarQuizRepository extends JpaRepository<SimilarQuiz, Long> {
}
