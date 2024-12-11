/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vnavesnoj.stomp_status_ping.config.properties.WsAuthenticationProperties;
import vnavesnoj.stomp_status_ping.security.HeaderWsAuthenticationConverter;
import vnavesnoj.stomp_status_ping.security.HeaderWsMessageMatcher;
import vnavesnoj.stomp_status_ping.websocket.WsAuthenticationInterceptor;

import java.util.Collections;
import java.util.List;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(WsAuthenticationProperties.class)
@ConditionalOnMissingBean(value = RemoteTokenWsAuthConfig.class)
@Configuration
public class UsernameHeaderWsAuthConfig {

    private final WsAuthenticationProperties properties;
    private final List<GrantedAuthority> defaultAuthorities =
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    @Bean
    WsAuthenticationInterceptor usernameHeaderWsAuthenticationInterceptor() {
        final var matcher = new HeaderWsMessageMatcher(properties.getUsernameHeader());
        final var converter = new HeaderWsAuthenticationConverter(
                properties.getUsernameHeader(),
                (username) -> UsernamePasswordAuthenticationToken.unauthenticated(username, null)
        );
        final AuthenticationManager manager = (authentication) ->
                UsernamePasswordAuthenticationToken.authenticated(
                        authentication.getPrincipal(),
                        null,
                        defaultAuthorities
                );
        return new WsAuthenticationInterceptor(matcher, converter, manager);
    }
}
