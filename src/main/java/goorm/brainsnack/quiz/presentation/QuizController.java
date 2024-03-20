package goorm.brainsnack.quiz.presentation;

import goorm.brainsnack.global.BaseResponse;
import goorm.brainsnack.quiz.dto.ChatGPTRequestDto;
import goorm.brainsnack.quiz.dto.QuizRequestDto.SingleGradeRequestDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto.CategoryQuizListDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto.GetTotalMemberDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto.MultiGradeDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto.SingleGradeDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import goorm.brainsnack.quiz.service.ChatGPTService;
import goorm.brainsnack.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static goorm.brainsnack.quiz.dto.QuizRequestDto.MultiGradeRequestDto;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.MultiResultResponseDto;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.QuizDetailDto;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    private final ChatGPTService chatGPTService;
    private static final String COMMENT_WITH_EXAMPLE = "위와 같은 형식으로 유사한 문제와 정답, " +
            "해설을 1개만 만들어줘. 양식은 위에처럼 문제 , 1번 , 2번 , 3번 , 4번 , 5번 , 정답 , 해설대로 해주고 각 항목당 줄바꿈은 한 번씩 해줘";
    private static final String COMMENT_NO_EXAMPLE = "위와 같은 형식으로 유사한 문제와 정답, " +
            "해설을 1개만 만들어줘. 양식은 위에처럼 문제 , 예시 , 1번 , 2번 , 3번 , 4번 , 5번 , 정답 , 해설대로 해주고 각 항목당 줄바꿈은 한 번씩 해줘";

    @GetMapping("/quiz/{quizId}/similar-quiz")
    public ResponseEntity<BaseResponse<SimilarQuizResponseDto.CreateDto>> createSimilarQuiz(@PathVariable Long quizId) {

        // 1. 문제 가져오고 GPT 에게 넘길 content 만들기
        QuizDetailDto quizDto = quizService.findQuiz(quizId);
        String content = createQuizTitle(quizDto);

        // 2. 1번에서 찾은 문제를 가지고 GPT 에 넘길 Dto 생성
        List<ChatGPTRequestDto.ChatRequestMsgDto> message = new ArrayList<>();
        message.add(new ChatGPTRequestDto.ChatRequestMsgDto("system" , content));
        ChatGPTRequestDto.ChatCompletionDto chatCompletionDto = new ChatGPTRequestDto.ChatCompletionDto("gpt-3.5-turbo-16k" , message);

        // 3. GPT API 에 전달 후 result 로 받기
        SimilarQuizResponseDto.CreateDto result = chatGPTService.prompt(chatCompletionDto, quizDto.getCategory());
        return ResponseEntity.ok(new BaseResponse<>(result));
    }
    private static String createQuizTitle(QuizDetailDto quizDto) {
        String content;
        if (quizDto.getExample().equals("X")) {
            content = "문제 : " + quizDto.getTitle() + "\n" + "1번 : " + quizDto.getChoiceFirst() + "\n" +
                    "2번 : " + quizDto.getChoiceSecond() + "\n" + "3번 : " + quizDto.getChoiceThird() + "\n" + "4번 : " + quizDto.getChoiceFourth() + "\n" +
                    "5번 : " + quizDto.getChoiceFifth() + "\n" + "정답 : " + quizDto.getAnswer() + "\n" + "해설 : " + quizDto.getSolution() + "\n" + COMMENT_NO_EXAMPLE;
        } else {
            content = "문제 : " + quizDto.getTitle() + "\n" + "예시 : " + quizDto.getExample() + "\n" + "1번 : " + quizDto.getChoiceFirst() + "\n" +
                    "2번 : " + quizDto.getChoiceSecond() + "\n" + "3번 : " + quizDto.getChoiceThird() + "\n" + "4번 : " + quizDto.getChoiceFourth() + "\n" +
                    "5번 : " + quizDto.getChoiceFifth() + "\n" + "정답 : " + quizDto.getAnswer() + "\n" + "해설 : " + quizDto.getSolution() + "\n" + COMMENT_WITH_EXAMPLE;
        }
        return content;
    }

    //영역별 모든 문제 조회
    @GetMapping("/quiz/{category}")
    public ResponseEntity<BaseResponse<CategoryQuizListDto>> getCategoryQuizzes(@PathVariable String category) {
        return ResponseEntity.ok().body(new BaseResponse<>(quizService.getCategoryQuizzes(category)));
    }

    //총 풀이 문제 조회
    @GetMapping("/members/{member-id}")
    public ResponseEntity<BaseResponse<GetTotalMemberDto>> getTotalMember(@PathVariable("member-id") Long memberId) {
        return ResponseEntity.ok().body(new BaseResponse<>(quizService.getTotalNum(memberId)));
    }

    //한 문제 채점
    @PostMapping("/members/{member-id}/quiz/{quiz-id}/grade")
    public ResponseEntity<BaseResponse<SingleGradeDto>> gradeSingleQuiz(@PathVariable("member-id") Long memberId,
                                                                        @PathVariable("quiz-id") Long quizId,
                                                                        @RequestBody SingleGradeRequestDto request) {
        return ResponseEntity.ok().body(new BaseResponse<>(quizService.gradeSingleQuiz(memberId, quizId, request)));
    }

    //여러 문제 채점
    @PostMapping("/members/{member-id}/quizzes/{category}/grade")
    public ResponseEntity<BaseResponse<MultiGradeDto>> gradeQuizzes(@PathVariable("member-id") Long memberId,
                                                                    @PathVariable("category") String category,
                                                                    @RequestBody MultiGradeRequestDto request) {
        return ResponseEntity.ok().body(new BaseResponse<>(quizService.gradeMultiQuiz(memberId, category, request)));
    }

    //전체 문제 채점 결과 리스트 조회
    @GetMapping("/members/{member-id}/quiz/{category}/grade")
    public ResponseEntity<BaseResponse<MultiResultResponseDto>> getFullQuizResult(@PathVariable("member-id") Long memberId,
                                                                                  @PathVariable("category") String category,
                                                                                  @RequestBody MultiGradeRequestDto request) {
        return ResponseEntity.ok().body(new BaseResponse<>(quizService.getFullResult(memberId, category, request)));
    }
}
