package goorm.brainsnack.quiz.dto;

import lombok.Builder;
import lombok.Getter;

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
        private Long quizId;
        private String category;
        private Boolean isSimilar;
        private int choice;
    }

}
