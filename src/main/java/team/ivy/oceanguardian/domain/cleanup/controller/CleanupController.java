package team.ivy.oceanguardian.domain.cleanup.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupRequest;
import team.ivy.oceanguardian.domain.cleanup.service.CleanupService;
import team.ivy.oceanguardian.domain.member.dto.MemberResponse;
import team.ivy.oceanguardian.domain.member.dto.SignUpRequest;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;

@RequestMapping("/cleanup")
@RestController
@RequiredArgsConstructor
public class CleanupController {
    private final CleanupService cleanupService;

//    @Operation(summary = "청소 데이터 생성", description = "청소 데이터를 생성하고, 이미지 파일을 S3에 저장합니다.")
//    @PostMapping("/save")
//    public ResponseEntity<ApiResponse<Long>> saveCleanup(
//        @RequestBody CleanupRequest cleanupRequest
//    ) throws IOException {
//        return ApiResponse.success(cleanupService.saveCleanup(cleanupRequest),"청소데이터 저장 성공");
//    }
}
