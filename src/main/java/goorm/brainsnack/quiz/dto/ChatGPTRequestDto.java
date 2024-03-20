package goorm.brainsnack.quiz.dto;

import lombok.*;

import java.util.List;

public class ChatGPTRequestDto {

    @Getter
    public static class ChatCompletionDto {
        // 사용할 모델
        private String model;
        private List<ChatRequestMsgDto> messages;

        @Builder
        public ChatCompletionDto(String model, List<ChatRequestMsgDto> messages) {
            this.model = model;
            this.messages = messages;
        }
    }

    @Getter
    public static class ChatRequestMsgDto {
        private String role;
        private String content;

        @Builder
        public ChatRequestMsgDto(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
