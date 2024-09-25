package team.ivy.oceanguardian.domain.cleanup.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringResponse;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;

@Data
@Builder
@Schema(description = "청소 데이터 응답용 DTO")
public class CleanupResponse {

    @Schema(description = "데이터 일련번호", example = "20240923162093e9c")
    private String serialNumber;
    @Schema(description = "청소 위치의 위도", example = "37.7749")
    private Double latitude;
    @Schema(description = "청소 위치의 경도", example = "127.4194")
    private Double longitude;
    @Schema(description = "해안 이름", example = "해운대")
    private String coastName;
    @Schema(description = "해안의 길이 (미터 단위)", example = "500.0")
    private Double coastLength;
    @Schema(description = "실제 쓰레기 양 (개수 단위)", example = "3")
    private Double actualTrashVolume;
    @Schema(description = "주요 쓰레기 타입 (1~5)", example = "2")
    private byte mainTrashType;
    @Schema(description = "청소전 이미지 url")
    private String beforeViewImageUrl;
    @Schema(description = "청소후 이미지 url")
    private String afterViewImageUrl;
    @Schema(description = "집하완료 이미지 url")
    private String completeViewImageUrl;

    public static CleanupResponse toDto(Cleanup cleanup, String beforeViewImageUrl, String afterViewImageUrl, String completeViewImageUrl ) {
        return CleanupResponse.builder()
            .serialNumber(cleanup.getSerialNumber())
            .latitude(cleanup.getLocation().getY())
            .longitude(cleanup.getLocation().getX())
            .coastName(cleanup.getCoastName())
            .coastLength(cleanup.getCoastLength())
            .actualTrashVolume(cleanup.getActualTrashVolume())
            .mainTrashType(cleanup.getMainTrashType())
            .beforeViewImageUrl(beforeViewImageUrl)
            .afterViewImageUrl(afterViewImageUrl)
            .completeViewImageUrl(completeViewImageUrl)
            .build();
    }


}
