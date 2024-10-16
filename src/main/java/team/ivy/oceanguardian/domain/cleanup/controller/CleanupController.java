package team.ivy.oceanguardian.domain.cleanup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupListResponse;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupRequest;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupResponse;
import team.ivy.oceanguardian.domain.cleanup.dto.CoastAvg;
import team.ivy.oceanguardian.domain.cleanup.service.CleanupService;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Tag(name="cleanup-controller", description = "청소 모드 관련 컨트롤러")
public class CleanupController {
    private final CleanupService cleanupService;

    @Operation(summary = "청소 데이터 생성", description = "요청값 중 mainTrashType에는 숫자(1~5)가 들어갑니다")
    @PostMapping(value = "/cleanup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> createCleanup(
        @RequestPart("cleanup") CleanupRequest cleanupRequest,
        @RequestPart(value = "beforeViewFile", required = false) MultipartFile beforeViewFile,
        @RequestPart(value = "afterViewFile", required = false) MultipartFile afterViewFile,
        @RequestPart(value = "completeViewFile", required = false) MultipartFile completeViewFile
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

    @Operation(summary = "전체 청소 리스트 조회", description = "전체 청소 데이터를 호출합니다(관리자용). page는 요청할 페이지 쪽수, size는 데이터의 갯수를 의미")
    @GetMapping(value = "/all-cleanup-list")
    public ResponseEntity<ApiResponse<CleanupListResponse>> getCleanupListLatest(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(cleanupService.getCleanupListLatest(pageable),"ADMIN용 청소 리스트 조회 성공");
    }

    @Operation(summary = "청소모드 해안명 자동완성", description = "키워드가 포함된 해안명을 반환합니다.(키워드는 음절 기준)")
    @GetMapping(value = "/cleanup/coast-name-list")
    public ResponseEntity<ApiResponse<List<String>>> getAutocompleteResults(
        @RequestParam String keyword
    ) {
        return ApiResponse.success(cleanupService.getAutocompleteResults(keyword),"해안명 자동완성 조회 성공");
    }
}
