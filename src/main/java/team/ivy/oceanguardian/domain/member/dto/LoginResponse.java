package team.ivy.oceanguardian.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringResponse;

@Data
@Builder
@Schema(description = "로그인 응답용 DTO")
public class LoginResponse {

    @Schema(description = "이름")
    private String name;
    @Schema(description = "역할")
    private String role;
    @Schema(description = "jwt 토큰")
    private JwtToken tokenInfo;

    public static LoginResponse toDto(Member member,JwtToken jwtToken) {
        return LoginResponse.builder()
            .name(member.getName())
            .role(member.getRoles().get(0))
            .tokenInfo(jwtToken)
            .build();
    }
}
