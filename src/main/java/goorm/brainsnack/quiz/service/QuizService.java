package goorm.brainsnack.quiz.service;

import java.util.List;

import static goorm.brainsnack.quiz.dto.MemberQuizResponseDto.MemberQuizDto;
import static goorm.brainsnack.quiz.dto.QuizRequestDto.*;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.*;
import static goorm.brainsnack.quiz.dto.SimilarQuizResponseDto.MemberSimilarQuizDto;

public interface QuizService {
    QuizDetailDto findQuiz(Long quizId);
    GetTotalMemberDto getTotalNum(Long memberId);
    CategoryQuizListDto getCategoryQuizzes(String category);

    SingleGradeDto gradeSingleQuiz(Long memberId, Long quizId, SingleGradeRequestDto request);

    SimilarQuizSingleGradeDto gradeSingleSimilarQuiz(Long memberId, Long quizId , SimilarQuizSingleGradeRequestDto request);
    MultiResultResponseDto gradeMultiQuiz(Long memberId, String category, MultiGradeRequestDto request);

    List<MemberQuizDto> getWrongQuizList(Long memberId , String category);
    List<MemberQuizDto> getCorrectQuizList(Long memberId , String category);
    MemberSimilarQuizDto getSimilarQuiz(Long memberId, String category , Long quizId);

    SingleGradeDto getSingleResult(Long memberId, Long quizId);
}
