package goorm.brainsnack.quiz.dto;

import lombok.Builder;
import lombok.Getter;

public class QuizResponseDto {

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
}
