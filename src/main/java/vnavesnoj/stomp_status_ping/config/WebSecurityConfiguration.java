package vnavesnoj.stomp_status_ping.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vnavesnoj.stomp_status_ping.security.filter.PrincipalHeaderAuthenticationFilter;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

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
                                .toArray(String[]::new)).authenticated()
                )
                .cors(withDefaults())
                .exceptionHandling(item -> item.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(
                        new PrincipalHeaderAuthenticationFilter(stompProperties.getPrincipalHeader()),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }
}
