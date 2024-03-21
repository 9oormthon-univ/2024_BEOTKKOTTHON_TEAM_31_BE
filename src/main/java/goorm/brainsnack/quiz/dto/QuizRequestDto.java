package goorm.brainsnack.quiz.dto;

import lombok.*;

import java.util.List;

public class QuizRequestDto {

    // NoArgsConstructor 가 있어야 FullGradeRequestDto 객체를 역직렬화할 때 인스턴스를 생성
    @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder @AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    public static class SimilarQuizSingleGradeRequestDto {
        private Long id;
        private int quizNum;
        private String category;
        private Boolean isSimilar;
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
