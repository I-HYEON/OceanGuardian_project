package team.ivy.oceanguardian.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.ivy.oceanguardian.domain.admin.service.AdminCleanupService;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupResponse;
import team.ivy.oceanguardian.domain.cleanup.dto.CoastAvg;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Tag(name="admin-cleanup-controller", description = "관리 모드 중 청소 관련 컨트롤러")
public class AdminCleanupController {
    private final AdminCleanupService adminCleanupService;

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
        return ApiResponse.success(adminCleanupService.getCleanupsBetween(startTime, endTime),"기간별 실쓰레기양 조회 성공");
    }

    @Operation(summary = "해안별 평균 수거량 데이터", description = "해안별 평균 수거량 데이터")
    @GetMapping(value = "/map/average")
    public ResponseEntity<ApiResponse<List<CoastAvg>>> getGroupedByCoastName() {
        return ApiResponse.success(adminCleanupService.getGroupedByCoastName(),"해안별 조회 성공");
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
        adminCleanupService.downloadCleanupData(startTime, endTime, response);
    }

    @Operation(summary = "평균 해안선길이 대비 평균 수거량 다운로드", description = "호출하면 평균 해안선길이 대비 평균 수거량 데이터가 xsl형식으로 다운로드됩니다.")
    @GetMapping(value = "/download/avg")
    public void downloadAvgData(HttpServletResponse response) throws IOException {
        adminCleanupService.downloadAvgData(response);
    }

}
