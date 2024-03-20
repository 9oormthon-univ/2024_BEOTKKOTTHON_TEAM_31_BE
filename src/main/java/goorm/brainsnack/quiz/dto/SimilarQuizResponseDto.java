package goorm.brainsnack.quiz.dto;

import goorm.brainsnack.quiz.domain.QuizCategory;
import lombok.*;

import java.util.List;

public class SimilarQuizResponseDto {

    @Builder @Getter
    public static class CreateDto {
        private int quizNum;
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

    @Builder @Getter
    public static class MemberSimilarQuizDto {
        // Member 관련된 필드
        private Long memberId;
        private String entryCode;
        // 유사 문제를 생성했을 때 어떤 카테고리의 quizNum 문제를 사용했는지
        private int quizNum;
        private int createSimilarQuizCount;

        private List<MemberQuizResponseDto.MemberQuizDto> memberQuizList;
    }
}
