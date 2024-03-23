package goorm.brainsnack.member.presentation;
import goorm.brainsnack.global.BaseResponse;
import goorm.brainsnack.member.service.MemberService;
import goorm.brainsnack.quiz.dto.MemberQuizResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static goorm.brainsnack.member.dto.MemberResponseDto.LoginDto;
import static goorm.brainsnack.quiz.dto.MemberQuizResponseDto.*;
import static goorm.brainsnack.quiz.dto.SimilarQuizResponseDto.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login/{temporaryId}")
    public ResponseEntity<BaseResponse<LoginDto>> login(@PathVariable String temporaryId) {
        return ResponseEntity.ok().body(new BaseResponse<>(memberService.login(temporaryId)));
    }

}
