package team.ivy.oceanguardian.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import team.ivy.oceanguardian.domain.member.security.CustomAccessDeniedHandler;
import team.ivy.oceanguardian.domain.member.security.CustomAuthenticationEntryPoint;
import team.ivy.oceanguardian.domain.member.security.JwtAuthenticationFilter;
import team.ivy.oceanguardian.domain.member.security.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
            .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
            .cors(Customizer.withDefaults()) //CORS 활성화
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 상태를 Stateless로 설정
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll() // Swagger 접근 허용
                .requestMatchers("/").permitAll()  // 건강 체크
                .requestMatchers("/login").permitAll()  // 로그인
                .requestMatchers("/signup").permitAll()  // 회원가입 엔드포인트는 인증 없이 접근 가능
//                .requestMatchers("/test").hasRole("USER")  // /test는 USER 권한 필요
                .anyRequest().authenticated()  // 그 외의 모든 요청은 인증 필요
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
            )
            .httpBasic(AbstractHttpConfigurer::disable)
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();  //필터 체인 빌드
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
