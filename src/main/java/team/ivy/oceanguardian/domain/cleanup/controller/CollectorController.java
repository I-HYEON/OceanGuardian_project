package team.ivy.oceanguardian.domain.cleanup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupListResponse;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupRequest;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupResponse;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupWithDistance;
import team.ivy.oceanguardian.domain.cleanup.dto.CoastAvg;
import team.ivy.oceanguardian.domain.cleanup.service.CleanupService;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;

@RestController
@RequiredArgsConstructor
@Tag(name="collector-controller", description = "수거 모드 관련 컨트롤러")
public class CollectorController {
    private final CleanupService cleanupService;

    @Operation(summary = "미수거 쓰레기 위치 데이터", description = "수거되지 않은 쓰레기 위치 표시")
    @GetMapping(value = "/map/not-pickup")
    public ResponseEntity<ApiResponse<List<CleanupResponse>>> getCleanupsNotPickup() {
        return ApiResponse.success(cleanupService.getCleanupsNotPickup(),"미수거 쓰레기 조회 성공");
    }

    @Operation(summary = "미수거 -> 수거 상태 변경", description = "쓰레기 pk값을 받아 해당 데이터의 pickup 상태를 수거완료로 변경합니다")
    @PatchMapping(value = "/pickup-do/{cleanupPk}")
    public ResponseEntity<ApiResponse<Void>> updatePickupStatusToTrue(
        @PathVariable Long cleanupPk
    ) {
        return ApiResponse.success(cleanupService.updatePickupStatusToTrue(cleanupPk),"쓰레기 수거 성공");
    }

    @Operation(summary = "수거 -> 미수거 상태 변경", description = "쓰레기 pk값을 받아 해당 데이터의 pickup 상태를 미수거로 변경합니다")
    @PatchMapping(value = "/pickup-undo/{cleanupPk}")
    public ResponseEntity<ApiResponse<Void>> updatePickupStatusToFalse(
        @PathVariable Long cleanupPk
    ) {
        return ApiResponse.success(cleanupService.updatePickupStatusToFalse(cleanupPk),"쓰레기 원상복구");
    }

    @Operation(summary = "가까운 쓰레기 조회", description = "사용자로부터 위도, 경도를 입력받아 가장 가까운 미수거 쓰레기를 반환합니다")
    @GetMapping("/closest-trash")
    public ResponseEntity<ApiResponse<CleanupWithDistance>> getCircleGeoJson(
        @Parameter(
            description = "위도",
            example = "35.1795",
            required = true
        )
        @RequestParam
        double latitude,
        @Parameter(
            description = "경도",
            example = "129.0756",
            required = true
        )
        @RequestParam
        double longitude
        ) {
        cleanupService.getClosestCleanup(latitude, longitude);

        return ApiResponse.success(cleanupService.getClosestCleanup(latitude, longitude),"가까운 쓰레기 조회 성공");

    }

}