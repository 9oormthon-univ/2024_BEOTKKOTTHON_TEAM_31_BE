package goorm.brainsnack.quiz.dto;

import goorm.brainsnack.quiz.domain.QuizCategory;
import lombok.*;

public class SimilarQuizResponseDto {

    @Builder @Getter
    public static class CreateDto {
        private QuizCategory category;
        private Boolean isSimilar;
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