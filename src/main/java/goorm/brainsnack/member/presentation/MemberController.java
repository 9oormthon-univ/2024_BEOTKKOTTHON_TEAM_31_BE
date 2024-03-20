package goorm.brainsnack.member.presentation;

import goorm.brainsnack.global.BaseResponse;
import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.member.service.MemberService;
import goorm.brainsnack.quiz.dto.MemberQuizResponseDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

import static goorm.brainsnack.member.dto.MemberResponseDto.LoginDto;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login/{temporaryId}")
    public ResponseEntity<BaseResponse<LoginDto>> login(@PathVariable String temporaryId) {
        return ResponseEntity.ok().body(new BaseResponse<>(memberService.login(temporaryId)));
    }

    // 내가 틀린 문제 조회 (기존 문제)
    @GetMapping("/member/{memberId}/quiz/wrong/{category}")
    public ResponseEntity<BaseResponse<List<MemberQuizResponseDto.MemberQuizDto>>> getWrongQuizList(@PathVariable Long memberId,
                                                                                        @PathVariable String category) {
        List<MemberQuizResponseDto.MemberQuizDto> quizDtoList = memberService.getWrongQuizList(memberId,category);
        return ResponseEntity.ok().body(new BaseResponse<>(quizDtoList));
    }

    // 내가 맞은 문제 조회 (기존 문제)
    @GetMapping("/member/{memberId}/quiz/correct")
    public ResponseEntity<BaseResponse<List<MemberQuizResponseDto.MemberQuizDto>>> getCorrectQuizList(@PathVariable Long memberId,
                                                                                                    @PathVariable String category) {
        List<MemberQuizResponseDto.MemberQuizDto> quizDtoList = memberService.getCorrectQuizList(memberId,category);
        return ResponseEntity.ok().body(new BaseResponse<>(quizDtoList));
    }

}
