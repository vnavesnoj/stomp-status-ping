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
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vnavesnoj.stomp_status_ping.config.properties.WsAuthenticationProperties;
import vnavesnoj.stomp_status_ping.security.HeaderWsAuthenticationConverter;
import vnavesnoj.stomp_status_ping.security.HeaderWsMessageMatcher;
import vnavesnoj.stomp_status_ping.security.TokenAuthenticationManager;
import vnavesnoj.stomp_status_ping.service.TokenPropertiesAuthenticationService;
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
public class LocalTokenWsAuthConfig {

    private final WsAuthenticationProperties properties;
    private static final String TOKENS_PROPERTIES = "classpath:tokens.properties";
    private final List<GrantedAuthority> defaultAuthorities =
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    @Bean
    WsAuthenticationInterceptor localTokenWsAuthenticationInterceptor(ResourceLoader resourceLoader) {
        final var matcher = new HeaderWsMessageMatcher(properties.getTokenHeader());
        final var converter = new HeaderWsAuthenticationConverter(
                properties.getTokenHeader(),
                (username) -> OneTimeTokenAuthenticationToken.unauthenticated(username, null)
        );
        final var tokenService = new TokenPropertiesAuthenticationService(TOKENS_PROPERTIES, resourceLoader);
        final AuthenticationManager manager = new TokenAuthenticationManager(
                tokenService,
                defaultAuthorities
        );
        return new WsAuthenticationInterceptor(matcher, converter, manager);
    }
}
