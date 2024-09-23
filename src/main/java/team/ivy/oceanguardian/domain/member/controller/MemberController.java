package team.ivy.oceanguardian.domain.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.ivy.oceanguardian.domain.member.dto.JwtToken;
import team.ivy.oceanguardian.domain.member.dto.LoginRequest;
import team.ivy.oceanguardian.domain.member.dto.MemberResponse;
import team.ivy.oceanguardian.domain.member.dto.SignUpRequest;
import team.ivy.oceanguardian.domain.member.service.MemberService;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Tag(name="Member", description = "회원 컨트롤러")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/hello")
    public ResponseEntity<ApiResponse<String>> getHello (){
        return ApiResponse.success("안녕하세요","조회 성공");
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> getTest (){
        return ApiResponse.success("로그인한 사람만 볼 수 있는","조회 성공");
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResponse>> signup(
        @RequestBody SignUpRequest signUpRequest
    ) {
        return ApiResponse.success(memberService.signUp(signUpRequest),"회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtToken>> login(
        @RequestBody LoginRequest loginRequest
    ) {
        String phoneNumber = loginRequest.getPhoneNumber();
        String password = loginRequest.getPassword();
        JwtToken jwtToken = memberService.login(phoneNumber, password);
        return ApiResponse.success(jwtToken,"로그인 성공");
    }
}
