package goorm.brainsnack.member.repository;

import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByTemporaryId(String temporaryId);

    @Query("select mq from MemberQuiz mq where mq.member.id = :memberId and mq.quiz.category = :category")
    List<MemberQuiz> findMemberQuizList(@Param("memberId") Long memberId , @Param("category") String category);
}
