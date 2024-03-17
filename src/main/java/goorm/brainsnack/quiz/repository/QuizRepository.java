package goorm.brainsnack.quiz.repository;


import goorm.brainsnack.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
