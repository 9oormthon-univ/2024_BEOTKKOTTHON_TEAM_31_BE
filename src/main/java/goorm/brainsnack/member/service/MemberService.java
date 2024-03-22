package goorm.brainsnack.member.service;

import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.quiz.dto.MemberQuizResponseDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;

import java.util.List;

import static goorm.brainsnack.quiz.dto.SimilarQuizResponseDto.*;

public interface MemberService {
    MemberResponseDto.LoginDto login(String temporaryId);

    MemberResponseDto.MemberDto findById(Long memberId);

    List<MemberQuizResponseDto.MemberQuizDto> getWrongQuizList(Long memberId , String category);
    List<MemberQuizResponseDto.MemberQuizDto> getCorrectQuizList(Long memberId , String category);
    MemberSimilarQuizDto getSimilarQuiz(Long memberId, String category , Long quizId);

    MemberSimilarQuizDto getSimilarQuizNoQuizId(Long memberId, String category , int quizNum);
}
