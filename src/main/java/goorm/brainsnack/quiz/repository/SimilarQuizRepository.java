package goorm.brainsnack.quiz.repository;

import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import goorm.brainsnack.quiz.domain.SimilarQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SimilarQuizRepository extends JpaRepository<SimilarQuiz, Long> {

    List<SimilarQuiz> findAllByCategory(QuizCategory category);

    @Query("select sq from SimilarQuiz sq JOIN FETCH sq.quiz q WHERE sq.quiz = :quizId and sq.category = :category")
    List<SimilarQuiz> findAllWithQuizId(@Param("quizId") Long quizId , QuizCategory category);

}
