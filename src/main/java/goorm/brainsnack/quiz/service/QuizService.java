package goorm.brainsnack.quiz.service;

import goorm.brainsnack.quiz.dto.QuizResponseDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto.GetTotalMemberDto;

import static goorm.brainsnack.quiz.dto.QuizRequestDto.*;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.*;

public interface QuizService {
    QuizResponseDto.QuizDto findQuiz(Long quizId);
    QuizResponseDto.GetTotalMemberDto getTotalNum(Long memberId);
    QuizResponseDto.QuizDetailDto findQuiz(Long quizId);
    GetTotalMemberDto getTotalNum(Long memberId);
    CategoryQuizListDto getCategoryQuizList(String category);

    SingleGradeDto gradeSingleQuiz(Long memberId, Long quizId, SingleGradeRequestDto request);

    FullGradeDto gradeFullQuiz(Long memberId, String category, FullGradeRequestDto request);
}
