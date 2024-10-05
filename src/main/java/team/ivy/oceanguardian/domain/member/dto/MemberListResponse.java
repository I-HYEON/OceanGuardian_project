package team.ivy.oceanguardian.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupSummary;

@Data
@Builder
@Schema(description = "사용자 리스트 응답용 DTO")
public class MemberListResponse {

    @Schema(description = "총 데이터 개수")
    private Long totalCount;
    @Schema(description = "현재 페이지")
    private Integer nowPage;
    @Schema(description = "최대 페이지")
    private Integer maxPage;
    @Schema(description = "데이터 리스트")
    private List<MemberResponse> userList;

}
