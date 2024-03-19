package goorm.brainsnack.quiz.dto;

import goorm.brainsnack.quiz.domain.QuizCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

public class QuizResponseDto {

    @Getter @Builder
    public static class QuizDto {
        private String title;
        private QuizCategory category;
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
