package team.ivy.oceanguardian.domain.cleanup.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;

@Data
@Builder
@Schema(description = "요약용 DTO")
public class CleanupSummary {

    @Schema(description = "pk값")
    private Long id;
    @Schema(description = "데이터 일련번호")
    private String serialNumber;
    @Schema(description = "해안 이름")
    private String coastName;
    @Schema(description = "작성 날짜")
    private LocalDateTime createdAt;
    @Schema(description = "수정 날짜")
    private LocalDateTime updatedAt;

    public static CleanupSummary toDto(Cleanup cleanup) {
        return CleanupSummary.builder()
            .id(cleanup.getId())
            .serialNumber(cleanup.getSerialNumber())
            .coastName(cleanup.getCoastName())
            .createdAt(cleanup.getCreatedAt())
            .updatedAt(cleanup.getUpdatedAt())
            .build();
    }

}
