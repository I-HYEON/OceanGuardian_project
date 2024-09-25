package team.ivy.oceanguardian.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.ivy.oceanguardian.domain.member.entity.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원 가입용 DTO")
public class SignUpRequest {

    @Schema(description = "전화번호", example = "01087654321")
    private String phoneNumber;
    @Schema(description = "비밀번호", example = "1234!")
    private String password;
    @Schema(description = "이름", example = "김지킴")
    private String name;
    @Schema(description = "역할", example = "[\"MONITOR\"]")
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword) {
        return Member.builder()
            .phoneNumber(phoneNumber)
            .password(encodedPassword)
            .name(name)
            .roles(roles)
            .build();
    }
}
