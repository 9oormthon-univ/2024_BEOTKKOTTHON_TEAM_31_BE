package goorm.brainsnack.member.service;

import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static goorm.brainsnack.member.dto.MemberResponseDto.LoginDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    @Transactional
    @Override
    public LoginDto login(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);

        //기존 멤버 확인
        if (optionalMember.isPresent()) {
            return Member.toMemberRequestDto(optionalMember.get());
        }

        Member member = Member.from(nickname);
        memberRepository.save(member);

        return Member.toMemberRequestDto(member);
    }

}
