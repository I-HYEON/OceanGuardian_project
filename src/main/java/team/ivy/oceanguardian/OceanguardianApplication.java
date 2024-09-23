package team.ivy.oceanguardian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OceanguardianApplication {

	public static void main(String[] args) {
		SpringApplication.run(OceanguardianApplication.class, args);
	}

}
