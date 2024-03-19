package goorm.brainsnack.quiz.presentation;

import goorm.brainsnack.global.BaseResponse;
import goorm.brainsnack.quiz.dto.QuizResponseDto.GetTotalMemberDto;
import goorm.brainsnack.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/members/{memberId}")
    public ResponseEntity<BaseResponse<GetTotalMemberDto>> getTotalMember(@PathVariable Long memberId) {
        return ResponseEntity.ok().body(new BaseResponse<>(quizService.getTotalNum(memberId)));
    }

}
