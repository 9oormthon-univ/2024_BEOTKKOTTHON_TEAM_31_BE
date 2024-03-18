package goorm.brainsnack.quiz.presentation;

import goorm.brainsnack.global.BaseResponse;
import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.dto.SimilarQuizRequestDto;
import goorm.brainsnack.quiz.service.ChatGPTService;
import goorm.brainsnack.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QuizController {

//    private final QuizService quizService;
//    private final ChatGPTService chatGPTService;
//
//    @GetMapping("/api/quiz/{quizId}/similar-quiz")
//    public ResponseEntity<BaseResponse<>> sample(@PathVariable Long quizId) {
//        // 해당 문제 ID 가 넘어온다.
//        // 그 문제 ID 를 가지고 문제를 찾고 -> CHAT GPT API 로 그 문제를 넘겨 -> 그리고 생성된 문제를 파싱해서 새로운 문제로 만들고 -> 그걸 반환해주면 된다.
//
//        // 1. 문제 가져오기
//        Quiz quiz = quizService.findQuiz(quizId);
//
//        String title = quiz.getTitle();
//
//
//        SimilarQuizRequestDto similarQuizRequestDto = new SimilarQuizRequestDto("gpt-3.5-turbo", content);
//
//        // 2. 그 문제를 chatGPT.prompt 로 넘기기
//
//        Map<String, Object> result = chatGPTService.prompt(chatCompletionDto);
//        // 2. 그 문제를 가지고 CHAT GPT API 프롬프트로 넘긴다.
//
//
//    }

}
