package goorm.brainsnack.quiz.dto;

import goorm.brainsnack.quiz.domain.QuizCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import goorm.brainsnack.quiz.domain.Quiz;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;


public class QuizResponseDto {

    @Getter
    @Builder
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

    @Getter
    @Builder
    public static class GetTotalMemberDto {
        private int totalQuizNum;

        public static GetTotalMemberDto from(int num) {
            return GetTotalMemberDto.builder()
                    .totalQuizNum(num)
                    .build();
        }
    }

    //영역별 문제 리스트 조회 dto
    @Getter
    @Builder
    public static class CategoryQuizListDto {
        private int size;
        private List<QuizDetailDto> quizDetailDtoList;

        public static CategoryQuizListDto of(int quizSize, List<QuizDetailDto> quizDetailDtoList) {
            return CategoryQuizListDto.builder()
                    .size(quizSize)
                    .quizDetailDtoList(quizDetailDtoList)
                    .build();
        }
    }

    //풀이용 문제 조회 dto
    @Getter
    @Builder
    public static class QuizDetailDto {
        private Long quizId;
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


    public static QuizDetailDto from(Quiz quiz) {
        return QuizDetailDto.builder()
                .quizId(quiz.getId())
                .quizNum(quiz.getQuizNum())
                .category(quiz.getCategory().name())
                .title(quiz.getTitle())
                .example(quiz.getExample())
                .choiceFirst(quiz.getChoiceFirst())
                .choiceSecond(quiz.getChoiceSecond())
                .choiceThird(quiz.getChoiceThird())
                .choiceFourth(quiz.getChoiceFourth())
                .choiceFifth(quiz.getChoiceFifth())
                .build();
    }
}
