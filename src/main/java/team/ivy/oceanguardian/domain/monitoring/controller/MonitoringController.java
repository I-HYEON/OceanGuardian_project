package team.ivy.oceanguardian.domain.monitoring.controller;

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
import org.springframework.web.bind.annotation.PatchMapping;
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
@Tag(name="monitoring-controller", description = "조사 / 관리 모드 관련 컨트롤러")
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

//    @Operation(summary = "조사 데이터 개별 수정", description = "조사 데이터의 일부를 수정합니다")
//    @PatchMapping (value = "/monitoring")
//    public ResponseEntity<ApiResponse<Long>> updateMonitoring(
//        @RequestPart("monitoring") MonitoringRequest monitoringRequest,
//        @RequestPart("image") MultipartFile imageFile
//    ) {
//        return ApiResponse.success(monitoringService.updateMonitoring(monitoringRequest, imageFile),"조사 데이터 수정 성공");
//    }

    @Operation(summary = "조사 데이터 개별 삭제", description = "조사 데이터를 삭제합니다")
    @DeleteMapping(value = "/monitoring/{monitoringId}")
    public ResponseEntity<ApiResponse<Void>> deleteMonitoring(
        @PathVariable Long monitoringId
    ) {
        return ApiResponse.success(monitoringService.deleteMonitoring(monitoringId),"조사 데이터 삭제 성공");
    }

    @Operation(summary = "지도용 예측(조사) 양 데이터", description = "위치 기반 예측(조사) 쓰레기양 표시")
    @GetMapping(value = "/map/monitorings")
    public ResponseEntity<ApiResponse<List<MonitoringResponse>>> getMonitoringsBetween(
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
        return ApiResponse.success(monitoringService.getMonitoringsBetween(startTime, endTime),"기간별 예측 쓰레기양 조회 성공");
    }

    @Operation(summary = "조사 데이터 다운로드", description = "호출하면 조건에 맞는 조사데이터가 xsl형식으로 다운로드됩니다.")
    @GetMapping(value = "/download/monitoring")
    public void downloadMonitoringData(
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
        monitoringService.downloadMonitoringData(startTime, endTime, response);
    }

    @Operation(summary = "관리자용 조사 데이터 리스트 조회", description = "page는 요청할 페이지 쪽수, size는 데이터의 갯수를 의미")
    @GetMapping(value = "/admin/monitoring-list")
    public ResponseEntity<ApiResponse<MonitoringListResponse>> getMonitoringListLatest(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(monitoringService.getMonitoringListLatest(pageable),"ADMIN용 조사 리스트 조회 성공");
    }

    @Operation(summary = "미해결 -> 해결 상태 변경", description = "조사 pk값을 받아 해당 데이터의 isResolved 상태를 해결로 변경합니다")
    @PatchMapping(value = "/monitoring-solved/{monitoringPk}")
    public ResponseEntity<ApiResponse<Long>> updateSolveStatusToTrue(
        @PathVariable Long monitoringPk
    ) {
        return ApiResponse.success(monitoringService.updateSolveStatusToTrue(monitoringPk),"조사한 쓰레기 해결");
    }

    @Operation(summary = "해결 -> 미해결 상태 변경", description = "조사 pk값을 받아 해당 데이터의 isResolved 상태를 미해결로 변경합니다")
    @PatchMapping(value = "/monitoring-unresolved/{monitoringPk}")
    public ResponseEntity<ApiResponse<Long>> updateSolveStatusToFalse(
        @PathVariable Long monitoringPk
    ) {
        return ApiResponse.success(monitoringService.updateSolveStatusToFalse(monitoringPk),"조사한 쓰레기 미해결");
    }

    @Operation(summary = "조사모드 해안명 자동완성", description = "키워드가 포함된 해안명을 반환합니다.(키워드는 음절 기준)")
    @GetMapping(value = "/monitoring/coast-name-list")
    public ResponseEntity<ApiResponse<List<String>>> getAutocompleteResults(
        @RequestParam String keyword
    ) {
        return ApiResponse.success(monitoringService.getAutocompleteResults(keyword),"해안명 자동완성 조회 성공");
    }
}
