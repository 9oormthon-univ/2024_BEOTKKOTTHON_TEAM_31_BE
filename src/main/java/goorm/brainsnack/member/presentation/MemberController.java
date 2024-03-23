package goorm.brainsnack.member.presentation;

import goorm.brainsnack.global.BaseResponse;
import goorm.brainsnack.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static goorm.brainsnack.member.dto.MemberResponseDto.LoginDto;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login/{nickname}")
    public ResponseEntity<BaseResponse<LoginDto>> login(@PathVariable String nickname) {
        return ResponseEntity.ok().body(new BaseResponse<>(memberService.login(nickname)));
    }

}
