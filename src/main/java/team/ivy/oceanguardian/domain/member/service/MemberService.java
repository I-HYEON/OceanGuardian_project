package team.ivy.oceanguardian.domain.member.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.ivy.oceanguardian.domain.member.dto.JwtToken;
import team.ivy.oceanguardian.domain.member.dto.LoginResponse;
import team.ivy.oceanguardian.domain.member.dto.MemberListResponse;
import team.ivy.oceanguardian.domain.member.dto.MemberResponse;
import team.ivy.oceanguardian.domain.member.dto.SignUpRequest;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.domain.member.repository.MemberRepository;
import team.ivy.oceanguardian.domain.member.security.JwtTokenProvider;
import team.ivy.oceanguardian.global.exception.CustomException;
import team.ivy.oceanguardian.global.exception.errorcode.ErrorCode;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public MemberResponse signUp(SignUpRequest signUpRequest) {
        if (memberRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
        }
        // password 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        return MemberResponse.toDto(memberRepository.save(signUpRequest.toEntity(encodedPassword)));
    }

    public LoginResponse login(String phoneNumber, String password) {
        // 사용자 존재 여부 확인
        Member member = memberRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 비밀번호 맞는지 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_ERROR);
        }

        //phoneNumber + password 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber,password);
        // authenticationToken으로 검증 진행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return LoginResponse.toDto(member,jwtTokenProvider.generateToken(authentication));

    }

    public MemberListResponse getUserListPage(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findByRolesContaining("USER", pageable);
        Page<MemberResponse> memberResponsePage = memberPage.map(MemberResponse::toDto);

        return MemberListResponse.builder()
            .maxPage(memberPage.getTotalPages())
            .nowPage(memberPage.getNumber())
            .totalCount(memberPage.getTotalElements())
            .userList(memberResponsePage.getContent())
            .build();
    }

    public List<MemberResponse> getUserList() {
        List<Member> members = memberRepository.findByRolesContaining("USER", Sort.by("name").ascending());

        return members.stream().map(MemberResponse::toDto).toList();
    }

}
