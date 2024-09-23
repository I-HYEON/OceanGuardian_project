package team.ivy.oceanguardian.domain.member.dto;

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
public class SignUpRequest {
    private String phoneNumber;
    private String password;
    private String name;
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
