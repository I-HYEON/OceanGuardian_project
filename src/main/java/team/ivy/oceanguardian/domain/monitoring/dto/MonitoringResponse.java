package team.ivy.oceanguardian.domain.monitoring.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;

@Data
@Builder
@Schema(description = "조사 데이터 응답용 DTO")
public class MonitoringResponse {

    @Schema(description = "pk값", example = "1")
    private Long id;
    @Schema(description = "데이터 일련번호", example = "20240923162093e9c")
    private String serialNumber;
    @Schema(description = "조사 위치의 위도", example = "37.7749")
    private Double latitude;
    @Schema(description = "조사 위치의 경도", example = "127.4194")
    private Double longitude;
    @Schema(description = "해안 이름", example = "해운대")
    private String coastName;
    @Schema(description = "해안의 길이 (미터 단위)", example = "500.0")
    private Double coastLength;
    @Schema(description = "예측된 쓰레기 양 (리터 단위)", example = "15")
    private Double predictedTrashVolume;
    @Schema(description = "주요 쓰레기 타입 (1~5)", example = "2")
    private byte mainTrashType;
    @Schema(description = "작성 일자")
    private LocalDateTime createdAt;
    @Schema(description = "수정 일자")
    private LocalDateTime updatedAt;
    @Schema(description = "작성자 이름")
    private String author;
    @Schema(description = "전경 이미지 url")
    private String monitoringImageUrl;

    public static MonitoringResponse toDto(Monitoring monitoring, String monitoringImageUrl) {
        return MonitoringResponse.builder()
            .id(monitoring.getId())
            .serialNumber(monitoring.getSerialNumber())
            .latitude(monitoring.getLocation().getY())
            .longitude(monitoring.getLocation().getX())
            .coastName(monitoring.getCoastName())
            .coastLength(monitoring.getCoastLength())
            .predictedTrashVolume(monitoring.getPredictedTrashVolume())
            .mainTrashType(monitoring.getMainTrashType())
            .createdAt(monitoring.getCreatedAt())
            .updatedAt(monitoring.getUpdatedAt())
            .author(monitoring.getMember().getName())
            .monitoringImageUrl(monitoringImageUrl)
            .build();
    }

    public static MonitoringResponse toDto(Monitoring monitoring) {
        return MonitoringResponse.builder()
            .id(monitoring.getId())
            .serialNumber(monitoring.getSerialNumber())
            .latitude(monitoring.getLocation().getY())
            .longitude(monitoring.getLocation().getX())
            .coastName(monitoring.getCoastName())
            .coastLength(monitoring.getCoastLength())
            .predictedTrashVolume(monitoring.getPredictedTrashVolume())
            .mainTrashType(monitoring.getMainTrashType())
            .monitoringImageUrl(null)
            .build();
    }

}
