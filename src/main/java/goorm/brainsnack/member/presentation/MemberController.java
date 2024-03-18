package goorm.brainsnack.member.presentation;

import goorm.brainsnack.global.BaseResponse;
import goorm.brainsnack.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static goorm.brainsnack.member.dto.MemberResponseDto.LoginDto;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login/{temporary-id}")
    public ResponseEntity<BaseResponse<LoginDto>> login(@PathVariable("temporary-id") String temporaryId) {
        return ResponseEntity.ok().body(new BaseResponse<>(memberService.login(temporaryId)));
    }



}
