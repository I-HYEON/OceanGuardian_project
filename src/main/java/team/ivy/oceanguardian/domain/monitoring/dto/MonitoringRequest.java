package team.ivy.oceanguardian.domain.monitoring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "조사 데이터 저장용 DTO")
public class MonitoringRequest {

    @Schema(description = "조사 위치의 위도", example = "37.7749")
    private Double latitude;
    @Schema(description = "조사 위치의 경도", example = "127.4194")
    private Double longitude;
    @Schema(description = "해안 이름", example = "해운대 해수욕장")
    private String coastName;
    @Schema(description = "해안의 길이 (미터 단위)", example = "500.0")
    private Double coastLength;
    @Schema(description = "예측된 쓰레기 양 (리터 단위)", example = "15")
    private Double predictedTrashVolume;
    @Schema(description = "주요 쓰레기 타입 (1~5)", example = "2")
    private byte mainTrashType;

    public Monitoring toEntity(String serialNumber, Point location, Member member) {
        return Monitoring.builder()
            .serialNumber(serialNumber)
            .location(location)
            .coastName(coastName)
            .coastLength(coastLength)
            .predictedTrashVolume(predictedTrashVolume)
            .mainTrashType(mainTrashType)
            .member(member)
            .build();
    }

}
