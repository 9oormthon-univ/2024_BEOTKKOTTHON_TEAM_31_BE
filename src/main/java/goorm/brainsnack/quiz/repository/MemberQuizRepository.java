package goorm.brainsnack.quiz.repository;

import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long> {

    List<MemberQuiz> findAllByMember(Member member);
}
