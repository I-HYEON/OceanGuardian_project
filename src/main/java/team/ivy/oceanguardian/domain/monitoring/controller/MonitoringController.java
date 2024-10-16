package team.ivy.oceanguardian.domain.monitoring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringListResponse;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringRequest;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringResponse;
import team.ivy.oceanguardian.domain.monitoring.service.MonitoringService;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Tag(name="monitoring-controller", description = "조사 모드 관련 컨트롤러")
public class MonitoringController {

    private final MonitoringService monitoringService;

    @Operation(summary = "조사 데이터 생성", description = "요청값 중 mainTrashType에는 숫자(1~5)가 들어갑니다")
    @PostMapping(value = "/monitoring", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> createMonitoring(
        @RequestPart("monitoring") MonitoringRequest monitoringRequest,
        @RequestPart(value = "monitoringViewFile", required = false) MultipartFile monitoringViewFile
    ) throws IOException {
        return ApiResponse.success(monitoringService.createMonitoring(monitoringRequest, monitoringViewFile),"조사 데이터 저장 성공");
    }

    @Operation(summary = "조사 데이터 리스트 조회", description = "page는 요청할 페이지 쪽수, size는 데이터의 갯수를 의미")
    @GetMapping(value = "/monitoring-list")
    public ResponseEntity<ApiResponse<MonitoringListResponse>> getMonitoringList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(monitoringService.getMonitoringList(pageable),"조사 리스트 조회 성공");
    }

    @Operation(summary = "조사 데이터 개별 조회", description = "조사 데이터의 id 값을 받아 세부사항을 반환합니다")
    @GetMapping(value = "/monitoring/{monitoringId}")
    public ResponseEntity<ApiResponse<MonitoringResponse>> getMonitoring(
        @PathVariable Long monitoringId
    ) {
        return ApiResponse.success(monitoringService.getMonitoring(monitoringId),"조사 데이터 조회 성공");
    }

    @Operation(summary = "조사 데이터 개별 삭제", description = "조사 데이터를 삭제합니다")
    @DeleteMapping(value = "/monitoring/{monitoringId}")
    public ResponseEntity<ApiResponse<Void>> deleteMonitoring(
        @PathVariable Long monitoringId
    ) {
        return ApiResponse.success(monitoringService.deleteMonitoring(monitoringId),"조사 데이터 삭제 성공");
    }

    @Operation(summary = "조사모드 해안명 자동완성", description = "키워드가 포함된 해안명을 반환합니다.(키워드는 음절 기준)")
    @GetMapping(value = "/monitoring/coast-name-list")
    public ResponseEntity<ApiResponse<List<String>>> getAutocompleteResults(
        @RequestParam String keyword
    ) {
        return ApiResponse.success(monitoringService.getAutocompleteResults(keyword),"해안명 자동완성 조회 성공");
    }

    @Operation(summary = "전체 조사 리스트 조회", description = "전체 조사 데이터를 호출합니다(관리자용). page는 요청할 페이지 쪽수, size는 데이터의 갯수를 의미")
    @GetMapping(value = "/all-monitoring-list")
    public ResponseEntity<ApiResponse<MonitoringListResponse>> getMonitoringListLatest(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(monitoringService.getMonitoringListLatest(pageable),"ADMIN용 조사 리스트 조회 성공");
    }
}
