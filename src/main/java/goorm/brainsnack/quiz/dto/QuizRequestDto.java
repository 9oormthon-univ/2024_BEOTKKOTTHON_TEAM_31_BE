package goorm.brainsnack.quiz.dto;

import lombok.*;

import java.util.List;

public class QuizRequestDto {

    @Getter
    @Builder
    public static class MultiGradeRequestDto {
        private String category;
        private List<SingleGradeRequestDto> gradeRequests;
    }

    @Getter
    @Builder
    public static class SingleGradeRequestDto {
        private Long id;
        private String category;
        private int choice;
    }

    @Getter
    @Builder
    public static class SimilarQuizSingleGradeRequestDto {
        private int choice;
        private String title;
        private String example;
        private String choiceFirst;
        private String choiceSecond;
        private String choiceThird;
        private String choiceFourth;
        private String choiceFifth;
        private int answer;
        private String solution;
    }
}
