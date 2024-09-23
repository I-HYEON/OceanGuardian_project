package team.ivy.oceanguardian.domain.member.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String phoneNumber;
    private String password;
}
