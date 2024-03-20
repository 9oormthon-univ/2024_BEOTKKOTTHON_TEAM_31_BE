package goorm.brainsnack.quiz.dto;


import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.quiz.domain.Quiz;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

public class MemberQuizResponseDto {


    @Getter @Builder

    public static class MemberQuizDto {
        private int quizNum;
        private String solution;
        // 풀었던 유사 문제도 있어야 함
    }
}
