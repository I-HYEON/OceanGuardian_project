package team.ivy.oceanguardian.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import team.ivy.oceanguardian.domain.member.entity.Member;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponse {

    private Long id;
    private String name;
    private String phoneNumber;

    static public MemberResponse toDto(Member member) {
        return MemberResponse.builder()
            .id(member.getId())
            .phoneNumber(member.getPhoneNumber())
            .name(member.getName())
            .build();
    }

}
