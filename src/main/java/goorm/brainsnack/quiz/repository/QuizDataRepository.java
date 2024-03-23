package goorm.brainsnack.quiz.repository;

import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizData;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface QuizDataRepository extends JpaRepository<QuizData, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<QuizData> findByQuiz(Quiz quiz);
}
