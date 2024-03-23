package goorm.brainsnack.member.service;

import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.quiz.dto.MemberQuizResponseDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;

import java.util.List;

import static goorm.brainsnack.quiz.dto.SimilarQuizResponseDto.*;

public interface MemberService {
    MemberResponseDto.LoginDto login(String temporaryId);
}
