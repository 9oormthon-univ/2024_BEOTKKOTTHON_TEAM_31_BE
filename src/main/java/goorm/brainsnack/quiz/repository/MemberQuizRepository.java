package goorm.brainsnack.quiz.repository;

import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long> {

    Optional<MemberQuiz> findByMemberAndQuiz(Member member, Quiz quiz);
    List<MemberQuiz> findAllByMember(Member member);
    @Query("SELECT mq FROM MemberQuiz mq " +
            "JOIN FETCH mq.member m JOIN FETCH mq.quiz q " +
            "WHERE m = :member AND q.category = :category")
    List<MemberQuiz> findAllByMemberAndCategory(@Param("member") Member member, @Param("category") QuizCategory category);

    @Query("SELECT mq From MemberQuiz mq " +
            "JOIN FETCH mq.member m JOIN FETCH mq.quiz q " +
            "WHERE m = :member AND mq.isCorrect = :isCorrect AND q.category = :category")
    List<MemberQuiz> findAllByMemberAndCategoryAndIsCorrect(
            @Param("member")Member member,
            @Param("isCorrect")Boolean isCorrect,
            @Param("category")QuizCategory category);


    @Query("SELECT mq From MemberQuiz mq " +
            "JOIN FETCH mq.quiz q " +
            "WHERE mq.member.id = :memberId AND q.category = :category")
    List<MemberQuiz> findMemberQuizList(@Param("memberId") Long memberId , @Param("category") QuizCategory category);


    @Query("SELECT mq From MemberQuiz mq " +
            "JOIN FETCH mq.quiz q " +
            "WHERE mq.member.id = :memberId AND mq.basedQuizId = :quizId AND q.isSimilar = TRUE and q.category = :category")
    List<MemberQuiz> findMemberSimilarQuiz(@Param(("memberId")) Long memberId , @Param("category") QuizCategory category ,
                                           @Param("quizId") Long quizId);
}
