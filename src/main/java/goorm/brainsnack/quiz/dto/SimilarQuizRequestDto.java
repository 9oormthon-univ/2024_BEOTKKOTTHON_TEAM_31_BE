package goorm.brainsnack.quiz.dto;

import lombok.*;

import java.util.List;
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class SimilarQuizRequestDto {
    private String model = "gpt-3.5-turbo";
    private String content;
}
