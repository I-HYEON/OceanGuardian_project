package team.ivy.oceanguardian.domain.monitoring.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringRequest;
import team.ivy.oceanguardian.domain.monitoring.service.MonitoringService;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;

@RequestMapping("/monitoring")
@RestController
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService monitoringService;

    @Operation(summary = "조사 데이터 생성", description = "조사 데이터를 생성하고, 이미지 파일을 S3에 저장합니다.")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> saveMonitoring(
        @RequestPart("monitoring") MonitoringRequest monitoringRequest,
        @RequestPart("image") MultipartFile imageFile
    ) throws IOException {
        return ApiResponse.success(monitoringService.saveMonitoring(monitoringRequest, imageFile),"조사 데이터 저장 성공");
    }

}
