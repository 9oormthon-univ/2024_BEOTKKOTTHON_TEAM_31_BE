package goorm.brainsnack.quiz.repository;

import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long> {

    List<MemberQuiz> findAllByMember(Member member);
    @Query("SELECT mq FROM MemberQuiz mq " +
            "JOIN FETCH mq.member m JOIN FETCH mq.quiz q " +
            "WHERE m = :member AND q.category = :category")
    List<MemberQuiz> findAllByMemberQuizAndCategory(@Param("member") Member member, @Param("category") QuizCategory category);

    @Query("SELECT mq From MemberQuiz mq " +
            "JOIN FETCH mq.member m JOIN FETCH mq.quiz q " +
            "WHERE m = :member AND mq.isCorrect = :isCorrect AND q.category = :category")
    List<MemberQuiz> findAllByMemberAndIsCorrectAndCategory(
            @Param("member")Member member,
            @Param("isCorrect")Boolean isCorrect,
            @Param("category")QuizCategory category);
}
