package goorm.brainsnack.quiz.dto;


import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.quiz.domain.Quiz;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

public class MemberQuizResponseDto {


    @Getter @Builder
    public static class MemberQuizDto {
        private int quizNum;
        private Long quizId;
    }

}
