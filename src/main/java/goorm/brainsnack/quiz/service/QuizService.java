package goorm.brainsnack.quiz.service;

import goorm.brainsnack.quiz.dto.QuizResponseDto;

import static goorm.brainsnack.quiz.dto.QuizRequestDto.*;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.*;

public interface QuizService {

    QuizResponseDto.QuizDetailDto findQuiz(Long quizId);
  
    GetTotalMemberDto getTotalNum(Long memberId);
    CategoryQuizListDto getCategoryQuizzes(String category);

    SingleGradeDto gradeSingleQuiz(Long memberId, Long quizId, SingleGradeRequestDto request);

    FullGradeDto gradeFullQuiz(Long memberId, String category, FullGradeRequestDto request);

    FullResultResponseDto getFullResult(Long memberId, String category, FullResultRequestDto request);
}
