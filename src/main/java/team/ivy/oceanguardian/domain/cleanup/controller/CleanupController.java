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
@Tag(name="cleanup-controller", description = "청소 / 관리 모드 관련 컨트롤러")
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

    @Operation(summary = "지도용 실쓰레기 양 데이터", description = "위치 기반 쓰레기양 표시")
    @GetMapping(value = "/map/cleanups")
    public ResponseEntity<ApiResponse<List<CleanupResponse>>> getCleanupsBetween(
        @Parameter(
            description = "시작 시간",
            example = "2017-11-01T00:00:00",
            required = true
        )
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startTime,
        @Parameter(
            description = "조회 종료 시간",
            example = "2017-12-02T23:59:59",
            required = true
        )
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endTime
    ) {
        return ApiResponse.success(cleanupService.getCleanupsBetween(startTime, endTime),"기간별 실쓰레기양 조회 성공");
    }

    @Operation(summary = "해안별 평균 수거량 데이터", description = "해안별 평균 수거량 데이터")
    @GetMapping(value = "/map/average")
    public ResponseEntity<ApiResponse<List<CoastAvg>>> getGroupedByCoastName() {
        return ApiResponse.success(cleanupService.getGroupedByCoastName(),"해안별 조회 성공");
    }

    @Operation(summary = "청소 데이터 다운로드", description = "호출하면 조건에 맞는 청소데이터가 xsl형식으로 다운로드됩니다.")
    @GetMapping(value = "/download/cleanup")
    public void downloadCleanupData(
        @Parameter(
            description = "시작 시간",
            example = "2017-11-01T00:00:00",
            required = true
        )
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startTime,
        @Parameter(
            description = "조회 종료 시간",
            example = "2017-12-02T23:59:59",
            required = true
        )
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endTime,
        HttpServletResponse response
    ) throws IOException {
        cleanupService.downloadCleanupData(startTime, endTime, response);
    }

    @Operation(summary = "평균 해안선길이 대비 평균 수거량 다운로드", description = "호출하면 평균 해안선길이 대비 평균 수거량 데이터가 xsl형식으로 다운로드됩니다.")
    @GetMapping(value = "/download/avg")
    public void downloadAvgData(HttpServletResponse response) throws IOException {
        cleanupService.downloadAvgData(response);
    }

    @Operation(summary = "관리자용 청소 리스트 조회", description = "page는 요청할 페이지 쪽수, size는 데이터의 갯수를 의미")
    @GetMapping(value = "/cleanup-list/admin")
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
