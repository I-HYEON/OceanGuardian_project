package team.ivy.oceanguardian.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "로그인 요청용 DTO")
public class LoginRequest {
    private String phoneNumber;
    private String password;
}
