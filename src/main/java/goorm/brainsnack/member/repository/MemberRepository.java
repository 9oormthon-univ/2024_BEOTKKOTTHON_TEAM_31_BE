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


    // 우선 묵시전 조인을 사용했고 명시적 조인으로 바꿔야한다면 바꿀 예정
    @Query("select mq from MemberQuiz mq where mq.member.id = :memberId and mq.quiz.category = :category")
    List<MemberQuiz> findMemberQuizList(@Param("memberId") Long memberId , @Param("category") QuizCategory category);

    @Query("select mq from MemberQuiz mq where mq.member.id = :memberId " +
            "and mq.quiz.id = :quizId and mq.quiz.isSimilar=TRUE and mq.quiz.category = :category")
    List<MemberQuiz> findMemberSimilarQuiz(@Param(("memberId")) Long memberId , @Param("quizId") Long quizId , @Param("category") QuizCategory category);
}
