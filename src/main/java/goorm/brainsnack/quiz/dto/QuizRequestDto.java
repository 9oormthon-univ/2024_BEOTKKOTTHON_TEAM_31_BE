package goorm.brainsnack.quiz.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class QuizRequestDto {

    @Getter
    @Builder
    public static class FullGradeRequestDto {
        private int size;
        private List<SingleGradeRequestDto> singleGradeRequestDtoList;
    }

    @Getter
    @Builder
    public static class SingleGradeRequestDto {
        private int quizNum;
        private String category;
        private Boolean isSimilar;
        private int choice;
    }

}
