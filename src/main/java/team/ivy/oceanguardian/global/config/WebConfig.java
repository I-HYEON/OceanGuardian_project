package team.ivy.oceanguardian.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:3000", "http://localhost:8080", "https://badajikimi.site", "http://badajikimi.vercel.app")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD")
			.allowedHeaders("Authorization", "Content-Type", "Accept", "X-Requested-With", "Cache-Control", "Origin", "Referer", "User-Agent", "Accept-Encoding", "Access-Control-Request-Headers")
			.exposedHeaders("Authorization")
			.allowCredentials(true);
	}
}
