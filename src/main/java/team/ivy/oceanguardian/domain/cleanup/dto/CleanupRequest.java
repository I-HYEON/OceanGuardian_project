package team.ivy.oceanguardian.domain.cleanup.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.member.entity.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "청소 데이터 저장용 DTO")
public class CleanupRequest {

    @Schema(description = "청소 위치의 위도", example = "37.7749")
    private Double latitude;
    @Schema(description = "청소 위치의 경도", example = "127.4194")
    private Double longitude;
    @Schema(description = "해안 이름", example = "해운대")
    private String coastName;
    @Schema(description = "해안의 길이 (미터 단위)", example = "500.0")
    private Double coastLength;
    @Schema(description = "실제 수거된 쓰레기 양 (개수 단위)", example = "3")
    private Double actualTrashVolume;
    @Schema(description = "주요 쓰레기 타입 (1~5)", example = "2")
    private byte mainTrashType;

    public Cleanup toEntity(String serialNumber, Point location, Member member) {
        return Cleanup.builder()
            .serialNumber(serialNumber)
            .location(location)
            .coastName(coastName)
            .coastLength(coastLength)
            .actualTrashVolume(actualTrashVolume)
            .mainTrashType(mainTrashType)
            .member(member)
            .build();
    }

}
