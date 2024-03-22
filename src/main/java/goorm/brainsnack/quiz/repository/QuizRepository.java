package goorm.brainsnack.quiz.repository;


import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findAllByCategoryAndIsSimilar(QuizCategory category, Boolean isSimilar);

    Optional<Quiz> findQuizByCategoryAndQuizNum(QuizCategory category , int quizNum);
    List<Quiz> findAllByCategory(QuizCategory category);

}
