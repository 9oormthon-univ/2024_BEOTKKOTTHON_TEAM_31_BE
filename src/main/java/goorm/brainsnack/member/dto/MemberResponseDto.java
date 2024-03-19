package goorm.brainsnack.member.dto;

import lombok.Builder;
import lombok.Getter;

public class MemberResponseDto {

    @Getter
    @Builder
    public static class LoginDto {
        private Long id;
        private String entryCode;
    }

    @Getter
    @Builder
    public static class MainViewDto {
        private int quizNum;
    }
}
