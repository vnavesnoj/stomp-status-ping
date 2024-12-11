package vnavesnoj.stomp_status_ping.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import vnavesnoj.stomp_status_ping.config.properties.StompWebSocketProperties;

import java.util.Arrays;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(value = "app.security.web.enable", havingValue = "true", matchIfMissing = true)
public class WebSecurityConfiguration {

    private final StompWebSocketProperties stompProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(item -> item.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(item -> item
                        .requestMatchers(Arrays.stream(stompProperties.getEndpoints())
                                .map(endpoint -> endpoint + "/**")
                                .toArray(String[]::new)).permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                )
                .exceptionHandling(item -> item.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .build();
    }
}
