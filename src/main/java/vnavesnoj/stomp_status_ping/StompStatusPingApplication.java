package vnavesnoj.stomp_status_ping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class StompStatusPingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StompStatusPingApplication.class, args);
	}

}
