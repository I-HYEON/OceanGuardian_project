package team.ivy.oceanguardian.domain.cleanup.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;

@Data
@Builder
@Schema(description = "해안별 데이터 DTO")
public class CoastAvg {

    @Schema(description = "해안 이름")
    private String coastName;
    @Schema(description = "해안선 길이 대비 평균 수거량")
    private Double avgTrashVolume;
    @Schema(description = "해안선 위도")
    private Double latitude;
    @Schema(description = "해안선 경도")
    private Double longitude;

    public static CoastAvg toDto(String coastName, Double avgTrashVolume, Double latitude, Double longitude) {
        return CoastAvg.builder()
            .coastName(coastName)
            .avgTrashVolume(avgTrashVolume)
            .latitude(latitude)
            .longitude(longitude)
            .build();
    }

}
