package team.ivy.oceanguardian.domain.cleanup.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupListResponse;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupRequest;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupResponse;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.cleanup.service.CleanupService;
import team.ivy.oceanguardian.domain.member.dto.MemberResponse;
import team.ivy.oceanguardian.domain.member.dto.SignUpRequest;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringListResponse;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringRequest;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringResponse;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
public class CleanupController {
    private final CleanupService cleanupService;

    @Operation(summary = "청소 데이터 생성", description = "요청값 중 mainTrashType에는 숫자(1~5)가 들어갑니다")
    @PostMapping(value = "/cleanup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> createCleanup(
        @RequestPart("cleanup") CleanupRequest cleanupRequest,
        @RequestPart("beforeViewFile") MultipartFile beforeViewFile,
        @RequestPart("afterViewFile") MultipartFile afterViewFile,
        @RequestPart("completeViewFile") MultipartFile completeViewFile
    ) throws IOException {
        return ApiResponse.success(cleanupService.createCleanup(cleanupRequest, beforeViewFile, afterViewFile, completeViewFile),"청소데이터 저장 성공");
    }

    @Operation(summary = "청소 데이터 리스트 조회", description = "page는 요청할 페이지 쪽수, size는 데이터의 갯수를 의미")
    @GetMapping(value = "/cleanup-list")
    public ResponseEntity<ApiResponse<CleanupListResponse>> getCleanupList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(cleanupService.getCleanupList(pageable),"청소 리스트 조회 성공");
    }

    @Operation(summary = "청소 데이터 개별 조회", description = "청소 데이터의 id 값을 받아 세부사항을 반환합니다")
    @GetMapping(value = "/cleanup/{cleanupId}")
    public ResponseEntity<ApiResponse<CleanupResponse>> getCleanup(
        @PathVariable Long cleanupId
    ) {
        return ApiResponse.success(cleanupService.getCleanup(cleanupId),"청소 데이터 조회 성공");
    }

    @Operation(summary = "청소 데이터 개별 삭제", description = "청소 데이터를 삭제합니다")
    @DeleteMapping(value = "/cleanup/{cleanupId}")
    public ResponseEntity<ApiResponse<Void>> deleteCleanup(
        @PathVariable Long cleanupId
    ) {
        return ApiResponse.success(cleanupService.deleteCleanup(cleanupId),"청소 데이터 삭제 성공");
    }

    @Operation(summary = "지도용 실쓰레기 양 데이터", description = "위치 기반 쓰레기양 표시")
    @GetMapping(value = "/map/cleanups")
    public ResponseEntity<ApiResponse<List<CleanupResponse>>> getCleanupsBetween(
        @RequestParam LocalDateTime startTIme,
        @RequestParam LocalDateTime endTime
    ) {
        return ApiResponse.success(cleanupService.getCleanupsBetween(startTIme, endTime),"기간별 실쓰레기양 조회 성공");
    }

    @Operation(summary = "미수거 쓰레기 위치 데이터", description = "수거되지 않은 쓰레기 위치 표시")
    @GetMapping(value = "/map/not-pickup")
    public ResponseEntity<ApiResponse<List<CleanupResponse>>> getCleanupsNotPickup() {
        return ApiResponse.success(cleanupService.getCleanupsNotPickup(),"미수거 쓰레기 조회 성공");
    }

    @Operation(summary = "미수거 -> 수거 상태 변경", description = "쓰레기 pk값을 받아 해당 데이터의 pickup 상태를 변경합니다")
    @PatchMapping(value = "/not-pickup/{cleanupPk}")
    public ResponseEntity<ApiResponse<Void>> updatePickupStatus(
        @PathVariable Long cleanupPk
    ) {
        return ApiResponse.success(cleanupService.updatePickupStatus(cleanupPk),"쓰레기 수거 성공");
    }

}
