package team.ivy.oceanguardian.domain.monitoring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;

@Data
@Builder
@Schema(description = "조사 리스트 응답용 DTO")
public class MonitoringListResponse {

    @Schema(description = "총 데이터 개수")
    private Long totalCount;
    @Schema(description = "현재 페이지")
    private Integer nowPage;
    @Schema(description = "최대 페이지")
    private Integer maxPage;
    @Schema(description = "데이터 리스트")
    private List<MonitoringSummary> monitoringList;

}
