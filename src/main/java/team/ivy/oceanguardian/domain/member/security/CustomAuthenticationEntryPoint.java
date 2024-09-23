package team.ivy.oceanguardian.domain.member.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;
import team.ivy.oceanguardian.global.exception.errorcode.ErrorCode;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 응답 형식을 JSON으로 지정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // ApiResponse 객체를 JSON으로 변환 후 응답
        String jsonResponse = new ObjectMapper().writeValueAsString(ApiResponse.fail(ErrorCode.NO_JWT_TOKEN.getMessage()));
        response.getWriter().write(jsonResponse);
    }
}
