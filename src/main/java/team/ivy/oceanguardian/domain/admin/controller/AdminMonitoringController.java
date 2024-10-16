package team.ivy.oceanguardian.domain.admin.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.ivy.oceanguardian.domain.admin.service.AdminMonitoringService;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringListResponse;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringResponse;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Tag(name="admin-monitoring-controller", description = "관리 모드 중 조사 관련 컨트롤러")
public class AdminMonitoringController {
    private final AdminMonitoringService adminMonitoringService;

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
        return ApiResponse.success(adminMonitoringService.getMonitoringsBetween(startTime, endTime),"기간별 예측 쓰레기양 조회 성공");
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
        adminMonitoringService.downloadMonitoringData(startTime, endTime, response);
    }

    @Operation(summary = "미해결 -> 해결 상태 변경", description = "조사 pk값을 받아 해당 데이터의 isResolved 상태를 해결로 변경합니다")
    @PatchMapping(value = "/monitoring-solved/{monitoringPk}")
    public ResponseEntity<ApiResponse<Long>> updateSolveStatusToTrue(
        @PathVariable Long monitoringPk
    ) {
        return ApiResponse.success(adminMonitoringService.updateSolveStatusToTrue(monitoringPk),"조사한 쓰레기 해결");
    }

    @Operation(summary = "해결 -> 미해결 상태 변경", description = "조사 pk값을 받아 해당 데이터의 isResolved 상태를 미해결로 변경합니다")
    @PatchMapping(value = "/monitoring-unresolved/{monitoringPk}")
    public ResponseEntity<ApiResponse<Long>> updateSolveStatusToFalse(
        @PathVariable Long monitoringPk
    ) {
        return ApiResponse.success(adminMonitoringService.updateSolveStatusToFalse(monitoringPk),"조사한 쓰레기 미해결");
    }

}
