package goorm.brainsnack.member.service;

import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.quiz.dto.MemberQuizResponseDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto;

import java.util.List;

public interface MemberService {
    MemberResponseDto.LoginDto login(String temporaryId);

    List<MemberQuizResponseDto.MemberQuizDto> getWrongQuizList(Long memberId , String category);
    List<MemberQuizResponseDto.MemberQuizDto> getCorrectQuizList(Long memberId , String category);
}
