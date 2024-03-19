package goorm.brainsnack.quiz.service;

import goorm.brainsnack.quiz.dto.QuizResponseDto;

public interface QuizService {
    QuizResponseDto.GetTotalMemberDto getTotalNum(Long memberId);
}
