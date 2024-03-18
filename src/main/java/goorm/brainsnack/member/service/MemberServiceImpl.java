package goorm.brainsnack.member.service;

import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public MemberResponseDto.LoginDto login(String temporaryId) {
        Optional<Member> optionalMember = memberRepository.findByTemporaryId(temporaryId);

        //기존 멤버 확인
        if (optionalMember.isPresent()) {
            return Member.toMemberRequestDto(optionalMember.get());
        }

        Member member = Member.from(temporaryId);
        memberRepository.save(member);

        return Member.toMemberRequestDto(member);
    }
}
