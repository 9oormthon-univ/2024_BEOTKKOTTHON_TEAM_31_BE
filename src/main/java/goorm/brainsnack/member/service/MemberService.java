package goorm.brainsnack.member.service;

import goorm.brainsnack.member.dto.MemberResponseDto;

public interface MemberService {
    MemberResponseDto.LoginDto login(String temporaryId);
}
