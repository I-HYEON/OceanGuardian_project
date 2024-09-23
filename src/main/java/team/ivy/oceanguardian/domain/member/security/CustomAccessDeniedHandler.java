package team.ivy.oceanguardian.domain.member.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import team.ivy.oceanguardian.global.apiresponse.ApiResponse;
import team.ivy.oceanguardian.global.exception.errorcode.ErrorCode;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 응답 형식을 JSON으로 지정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // ApiResponse 객체를 JSON으로 변환 후 응답
        String jsonResponse = new ObjectMapper().writeValueAsString(ApiResponse.fail(ErrorCode.NO_AUTHORITY.getMessage()));
        response.getWriter().write(jsonResponse);
    }

}
