package goorm.brainsnack.quiz.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class QuizRequestDto {

    @Getter
    @Builder
    public static class FullGradeRequestDto {
        private List<SingleGradeRequestDto> gradeRequestList;
    }

    @Getter
    @Builder
    public static class SingleGradeRequestDto {
        private Long id;
        private int quizNum;
        private String category;
        private Boolean isSimilar;
        private int choice;
    }

    @Getter
    @Builder
    public static class FullResultRequestDto {
        private String category;
        private List<SingleGradeRequestDto> gradeRequestList;
    }

}
