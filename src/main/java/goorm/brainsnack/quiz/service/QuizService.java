package goorm.brainsnack.quiz.service;

import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.quiz.dto.MemberQuizResponseDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;

import java.util.List;

import static goorm.brainsnack.quiz.dto.MemberQuizResponseDto.*;
import static goorm.brainsnack.quiz.dto.QuizRequestDto.*;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.*;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.SimilarQuizSingleGradeDto.*;
import static goorm.brainsnack.quiz.dto.SimilarQuizResponseDto.*;

public interface QuizService {
    QuizDetailDto findQuiz(Long quizId);
    GetTotalMemberDto getTotalNum(Long memberId);
    CategoryQuizListDto getCategoryQuizzes(String category);

    SingleGradeDto gradeSingleQuiz(Long memberId, Long quizId, SingleGradeRequestDto request);

    SimilarQuizSingleGradeDto gradeSingleSimilarQuiz(Long memberId, Long quizId , SimilarQuizSingleGradeRequestDto request);
    MultiGradeDto gradeMultiQuiz(Long memberId, String category, MultiGradeRequestDto request);

    MultiResultResponseDto getFullResult(Long memberId, String category);

    List<MemberQuizDto> getWrongQuizList(Long memberId , String category);
    List<MemberQuizDto> getCorrectQuizList(Long memberId , String category);
    MemberSimilarQuizDto getSimilarQuiz(Long memberId, String category , Long quizId);
}
