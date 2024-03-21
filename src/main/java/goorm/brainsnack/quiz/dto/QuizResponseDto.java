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
    public static class FullGradeDto {
        private List<SingleGradeDto> gradeList;

        public static FullGradeDto from(List<SingleGradeDto> list) {
            return FullGradeDto.builder()
                    .gradeList(list)
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

        private int quizParticipantsNum;
        private int correctAnswerNum;
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
                    .quizParticipantsNum(data.getQuizParticipantsNum())
                    .correctAnswerNum(data.getCorrectAnswerNum())
                    .ratioOfCorrect(ratio)
                    .build();
        }
    }


    @Getter
    @Builder
    public static class SimilarQuizSingleGradeDto {
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

        public static SimilarQuizSingleGradeDto of(Quiz quiz, MemberQuiz memberQuiz){
            return SimilarQuizSingleGradeDto.builder()
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
                    .build();
        }
    }
}
