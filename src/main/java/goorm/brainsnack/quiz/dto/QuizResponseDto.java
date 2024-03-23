package goorm.brainsnack.quiz.dto;

import goorm.brainsnack.quiz.domain.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


public class QuizResponseDto {

    @Getter
    @Builder
    public static class QuizDto {
        private String title;
        private String category;
        private String example;
        private String choiceFirst;
        private String choiceSecond;
        private String choiceThird;
        private String choiceFourth;
        private String choiceFifth;
        private int answer;
        private String solution;
        private int quizNum;
    }

    //영역별 문제 리스트 조회 dto
    @Getter
    @Builder
    public static class CategoryQuizListDto {
        private int size;
        private List<SingleQuizDto> quizzes;


        public static CategoryQuizListDto from(List<Quiz> quizzes) {
            return CategoryQuizListDto.builder()
                    .size(quizzes.size())
                    .quizzes(quizzes.stream()
                            .map(SingleQuizDto::from)
                            .toList())
                    .build();
        }
        public static CategoryQuizListDto of(int quizSize, List<SingleQuizDto> quizDtos) {
            return CategoryQuizListDto.builder()
                    .quizzes(quizDtos)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class QuizSimpleDto {
        private String title;
        private QuizCategory category;
    }

    @Getter
    @Builder
    public static class GetTotalMemberDto {
        private int totalQuizCounts;

        public static GetTotalMemberDto from(int num) {
            return GetTotalMemberDto.builder()
                    .totalQuizCounts(num)
                    .build();
        }
    }


    //문제 조회 dto
    @Getter
    @Builder
    public static class SingleQuizDto {
        private Long id;
        private int quizNum;
        private String category;
        private String title;
        private String example;
        private String choiceFirst;
        private String choiceSecond;
        private String choiceThird;
        private String choiceFourth;
        private String choiceFifth;

        public static SingleQuizDto from(Quiz quiz) {
            return SingleQuizDto.builder()
                    .id(quiz.getId())
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

    //문제 전체 내용 조회 dto
    @Getter
    @Builder
    public static class QuizDetailDto {
        private Long id;
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

        public static QuizDetailDto from(Quiz quiz) {
            return QuizDetailDto.builder()
                    .id(quiz.getId())
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


    @Getter
    @Builder
    public static class SimilarQuizSingleGradeDto {
        private Long id;
        private int quizNum;
        private String title;
        private String example;
        private String choiceFirst;
        private String choiceSecond;
        private String choiceThird;
        private String choiceFourth;
        private String choiceFifth;

        private Boolean isCorrect;
        private int userChoice;
        private int answer;
        private String solution;

        public static SimilarQuizSingleGradeDto of(SimilarQuiz similarQuiz, MemberQuiz memberQuiz) {
            return SimilarQuizSingleGradeDto.builder()
                    .id(similarQuiz.getId())

                    .quizNum(similarQuiz.getQuizNum())

                    .title(similarQuiz.getTitle())
                    .example(similarQuiz.getExample())
                    .choiceFirst(similarQuiz.getChoiceFirst())
                    .choiceSecond(similarQuiz.getChoiceSecond())
                    .choiceThird(similarQuiz.getChoiceThird())
                    .choiceFourth(similarQuiz.getChoiceFourth())
                    .choiceFifth(similarQuiz.getChoiceFifth())
                    .isCorrect(memberQuiz.getIsCorrect())
                    .userChoice(memberQuiz.getChoice())
                    .answer(similarQuiz.getAnswer())
                    .solution(similarQuiz.getSolution())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class SingleGradeDto {
        private Long id;
        private int quizNum;
        private String title;
        private String example;
        private String choiceFirst;
        private String choiceSecond;
        private String choiceThird;
        private String choiceFourth;
        private String choiceFifth;

        private Boolean isCorrect;
        private int userChoice;
        private int answer;
        private String solution;

        private int quizParticipantsCounts;
        private int correctAnswerCounts;
        private int ratioOfCorrect;

        public static SingleGradeDto of(Quiz quiz, MemberQuiz memberQuiz, QuizData data, int ratio) {
            return SingleGradeDto.builder()
                    .id(quiz.getId())
                    .quizNum(quiz.getQuizNum())
                    .title(quiz.getTitle())
                    .example(quiz.getExample())
                    .choiceFirst(quiz.getChoiceFirst())
                    .choiceSecond(quiz.getChoiceSecond())
                    .choiceThird(quiz.getChoiceThird())
                    .choiceFourth(quiz.getChoiceFourth())
                    .choiceFifth(quiz.getChoiceFifth())
                    .isCorrect(memberQuiz.getIsCorrect())
                    .userChoice(memberQuiz.getChoice())
                    .answer(quiz.getAnswer())
                    .solution(quiz.getSolution())
                    .quizParticipantsCounts(data.getQuizParticipantsCounts())
                    .correctAnswerCounts(data.getCorrectAnswerCounts())
                    .ratioOfCorrect(ratio)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class MultiResultResponseDto {
        private int totalQuizCounts;
        private int wrongQuizCounts;
        private String category;
        private List<SingleResultResponseDto> resultList;

        public static MultiResultResponseDto of(int totalQuizCounts, int wrongQuizCounts, List<SingleResultResponseDto> resultList, QuizCategory category) {
            return MultiResultResponseDto.builder()
                    .totalQuizCounts(totalQuizCounts)
                    .wrongQuizCounts(wrongQuizCounts)
                    .category(category.name())
                    .resultList(resultList)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class SingleResultResponseDto {
        private Long id;
        private int quizNum;
        private Boolean isCorrect;

        public static SingleResultResponseDto from(SingleGradeDto singleGrade) {
            return SingleResultResponseDto.builder()
                    .id(singleGrade.getId())
                    .quizNum(singleGrade.getQuizNum())
                    .isCorrect(singleGrade.getIsCorrect())
                    .build();
        }

        public static SingleResultResponseDto from(MemberQuiz memberQuiz) {
            Quiz quiz = memberQuiz.getQuiz();
            Long quizId = quiz.getId();

            return SingleResultResponseDto.builder()
                    .id(quizId)
                    .quizNum(quiz.getQuizNum())
                    .isCorrect(memberQuiz.getIsCorrect())
                    .build();
        }
    }
}

