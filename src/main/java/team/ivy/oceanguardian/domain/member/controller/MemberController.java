package team.ivy.oceanguardian.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.ivy.oceanguardian.domain.member.dto.JwtToken;
import team.ivy.oceanguardian.domain.member.dto.LoginRequest;
import team.ivy.oceanguardian.domain.member.dto.LoginResponse;
import team.ivy.oceanguardian.domain.member.dto.MemberResponse;
import team.ivy.oceanguardian.domain.member.dto.SignUpRequest;
import team.ivy.oceanguardian.domain.member.service.MemberService;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Tag(name="Member", description = "회원 컨트롤러")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "상태체크", description = "aws 로드밸런서의 상태체크용 api 입니다")
    @GetMapping("/")
    public ResponseEntity<ApiResponse<String>> getHealthCheck (){
        return ApiResponse.success("서버 건강함","조회 성공");
    }

    @Operation(summary = "테스트", description = "로그인이 되어있는지 확인할 수 있는 테스트용 api입니다")
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> getTest (){
        return ApiResponse.success("로그인한 사람만 볼 수 있는","조회 성공");
    }

    @Operation(summary = "회원 가입", description = "요청값 중 roles의 원소는 MONITOR,CLEANER,DRIVER,ADMIN 중 하나가 들어갑니다(추후 확장성 위해 리스트로 구현)")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResponse>> signup(
        @RequestBody SignUpRequest signUpRequest
    ) {
        return ApiResponse.success(memberService.signUp(signUpRequest),"회원가입 성공");
    }

    @Operation(summary = "로그인", description = "전화번호와 비밀번호를 사용해 로그인합니다")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
        @RequestBody LoginRequest loginRequest
    ) {
        String phoneNumber = loginRequest.getPhoneNumber();
        String password = loginRequest.getPassword();

        return ApiResponse.success(memberService.login(phoneNumber, password),"로그인 성공");
    }
}
