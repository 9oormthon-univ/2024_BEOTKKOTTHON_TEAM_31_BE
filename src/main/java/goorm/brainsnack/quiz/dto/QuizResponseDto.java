package goorm.brainsnack.quiz.dto;

import goorm.brainsnack.quiz.domain.MemberQuiz;
import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import goorm.brainsnack.quiz.domain.QuizData;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


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
        private int quizNum;
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

        //영역별 문제 리스트 조회 dto
        @Getter
        @Builder
        public static class CategoryQuizListDto {
            private int size;
            private List<SingleQuizDto> quizzes;

            public static CategoryQuizListDto of(int quizSize, List<SingleQuizDto> quizDtos) {
                return CategoryQuizListDto.builder()
                        .quizzes(quizDtos)
                        .build();
            }
        }

        //문제 조회 dto
        @Getter
        @Builder
        public static class SingleQuizDto {
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

            public static SingleQuizDto from(Quiz quiz) {
                return SingleQuizDto.builder()
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

        //문제 전체 내용 조회 dto
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

            @Getter
            @Builder
            public static class MultiGradeDto {
                private List<SingleGradeDto> quizzes;

                public static MultiGradeDto from(List<SingleGradeDto> results) {
                    return MultiGradeDto.builder()
                            .quizzes(results)
                            .build();
                }
            }

            @Getter
            @Builder
            public static class SingleGradeDto {
                private Long quizId;
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
                            .quizId(quiz.getId())
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
                private List<SingleResultResponseDto> results;

                public static MultiResultResponseDto of(int totalQuizCounts, int wrongQuizCounts, List<MemberQuiz> memberQuizzes, QuizCategory category) {
                    return MultiResultResponseDto.builder()
                            .totalQuizCounts(totalQuizCounts)
                            .wrongQuizCounts(wrongQuizCounts)
                            .category(category.name())
                            .results(memberQuizzes.stream()
                                    .map(SingleResultResponseDto::from)
                                    .toList())
                            .build();
                }
            }

            @Getter
            @Builder
            public static class SingleResultResponseDto {
                private Long quizId;
                private int quizNum;
                private Boolean isCorrect;

                public static SingleResultResponseDto from(MemberQuiz memberQuiz) {
                    Quiz quiz = memberQuiz.getQuiz();
                    Long quizId = quiz.getId();

                    return SingleResultResponseDto.builder()
                            .quizId(quizId)
                            .quizNum(quiz.getQuizNum())
                            .isCorrect(memberQuiz.getIsCorrect())
                            .build();
                }
            }
        }
