package goorm.brainsnack.member.repository;

import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import goorm.brainsnack.quiz.dto.MemberQuizResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByTemporaryId(String temporaryId);

    @Query("select mq from MemberQuiz mq join mq.quiz q where mq.member.id = :memberId and q.category = :category")
    List<MemberQuiz> findMemberQuizList(@Param("memberId") Long memberId , @Param("category") QuizCategory category);


    @Query("select mq from MemberQuiz mq join mq.quiz q where mq.member.id = :memberId and q.isSimilar = TRUE and q.category = :category and mq.basedQuizId= :quizId")
    List<MemberQuiz> findMemberSimilarQuiz(@Param(("memberId")) Long memberId , @Param("category") QuizCategory category , @Param("quizId") Long quizId);
}
