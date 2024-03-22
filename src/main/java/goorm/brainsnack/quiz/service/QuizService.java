package goorm.brainsnack.quiz.service;

import goorm.brainsnack.quiz.dto.QuizResponseDto;

import static goorm.brainsnack.quiz.dto.QuizRequestDto.*;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.*;

public interface QuizService {
    QuizResponseDto.QuizDetailDto findQuiz(Long quizId);
    GetTotalMemberDto getTotalNum(Long memberId);
    CategoryQuizListDto getCategoryQuizzes(String category);

    SimilarQuizSingleGradeDto.SingleGradeDto gradeSingleQuiz(Long memberId, Long quizId, SingleGradeRequestDto request);


    SimilarQuizSingleGradeDto gradeSingleSimilarQuiz(Long memberId, Long quizId , SimilarQuizSingleGradeRequestDto request);
    SimilarQuizSingleGradeDto.MultiGradeDto gradeMultiQuiz(Long memberId, String category, MultiGradeRequestDto request);

    SimilarQuizSingleGradeDto.MultiResultResponseDto getFullResult(Long memberId, String category);
}
