package goorm.brainsnack.quiz.dto;


import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import goorm.brainsnack.quiz.domain.Quiz;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

public class MemberQuizResponseDto {

    @Getter @Builder
    public static class MemberQuizDto {
        private int quizNum;
        private Long id;
    }

    /**
     * 풀었던 유사 문제가 있는지에 대한 유무 existence
     */
    @Getter @Builder
    public static class MemberQuizExistSimilarQuizDto {
        private Long id;
        private int quizNum;

        public static MemberQuizExistSimilarQuizDto from(MemberQuiz memberQuiz) {
            Quiz quiz = memberQuiz.getQuiz();
            return MemberQuizExistSimilarQuizDto.builder()
                    .id(quiz.getId())
                    .quizNum(quiz.getQuizNum())
                    .build();
        }
    }

    @Getter @Builder
    public static class MemberQuizWithIsCorrectDto {
        private int quizNum;
        private Long id;
        private Boolean isCorrect;
    }
}
