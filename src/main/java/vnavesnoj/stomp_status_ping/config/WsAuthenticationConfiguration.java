/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vnavesnoj.stomp_status_ping.config.properties.WsCredentialProperties;
import vnavesnoj.stomp_status_ping.security.HeaderWsMessageMatcher;
import vnavesnoj.stomp_status_ping.security.UsernameHeaderWsAuthenticationConverter;
import vnavesnoj.stomp_status_ping.websocket.WsAuthenticationInterceptor;

import java.util.Collections;
import java.util.List;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

@RequiredArgsConstructor
@EnableConfigurationProperties(WsCredentialProperties.class)
@Configuration
public class WsAuthenticationConfiguration {

    private final WsCredentialProperties properties;
    private final List<GrantedAuthority> defaultAuthorities =
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    @Bean
    WsAuthenticationInterceptor usernameHeaderWsAuthenticationInterceptor() {
        final var matcher = new HeaderWsMessageMatcher(properties.getUsernameHeader());
        final var converter = new UsernameHeaderWsAuthenticationConverter(properties.getUsernameHeader());
        final AuthenticationManager manager = (authentication) ->
                UsernamePasswordAuthenticationToken.authenticated(
                        authentication.getPrincipal(),
                        null,
                        defaultAuthorities
                );
        return new WsAuthenticationInterceptor(matcher, converter, manager);
    }
}
