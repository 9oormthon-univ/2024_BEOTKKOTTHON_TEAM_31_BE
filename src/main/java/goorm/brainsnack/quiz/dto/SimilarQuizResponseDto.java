package goorm.brainsnack.quiz.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static goorm.brainsnack.quiz.dto.MemberQuizResponseDto.*;

public class SimilarQuizResponseDto {

    @Builder @Getter
    public static class CreateDto {
        private int quizNum;
        private String category;
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
        private Long memberId;
        private String entryCode;
        // 유사 문제를 생성했을 때 어떤 카테고리의 quizNum 문제를 사용했는지
        private int quizNum;
        private int createSimilarQuizCount;

        private List<MemberQuizWithIsCorrectDto> memberQuizList;
    }
}
