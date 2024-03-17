package goorm.brainsnack.member.repository;

import goorm.brainsnack.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
