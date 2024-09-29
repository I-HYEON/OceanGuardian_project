package team.ivy.oceanguardian.domain.cleanup.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "가장 가까운 쓰레기 데이터 DTO")
public class CleanupWithDistance {

    @Schema(description = "해안 이름", example = "길천리1")
    private String coastName;
    @Schema(description = "청소 위치의 위도", example = "37.7749")
    private Double latitude;
    @Schema(description = "청소 위치의 경도", example = "127.4194")
    private Double longitude;
    @Schema(description = "해당 위치까지의 거리", example = "2.5")
    private double distance;

    public static CleanupWithDistance toDto(String coastName, Double latitude, Double longitude, Double distance) {
        return CleanupWithDistance.builder()
            .coastName(coastName)
            .latitude(latitude)
            .longitude(longitude)
            .distance(distance)
            .build();
    }

}
