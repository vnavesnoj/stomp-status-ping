/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.reactive.function.client.WebClient;
import vnavesnoj.stomp_status_ping.config.properties.AuthWebClientProperties;
import vnavesnoj.stomp_status_ping.config.properties.WsAuthenticationProperties;
import vnavesnoj.stomp_status_ping.security.HeaderWsAuthenticationConverter;
import vnavesnoj.stomp_status_ping.security.HeaderWsMessageMatcher;
import vnavesnoj.stomp_status_ping.security.TokenAuthenticationManager;
import vnavesnoj.stomp_status_ping.service.TokenRemoteAuthenticationService;
import vnavesnoj.stomp_status_ping.websocket.interceptor.WsAuthenticationInterceptor;

import java.util.Collections;
import java.util.List;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

@RequiredArgsConstructor
@EnableConfigurationProperties(WsAuthenticationProperties.class)
@ConditionalOnProperty(name = "app.websocket.stomp.auth.local", havingValue = "false", matchIfMissing = true)
@Configuration
public class RemoteTokenWsAuthConfig {

    private final WsAuthenticationProperties properties;
    private final List<GrantedAuthority> defaultAuthorities =
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    @Bean
    WsAuthenticationInterceptor remoteTokenWsAuthenticationInterceptor(WebClient webClient,
                                                                       ObjectMapper objectMapper,
                                                                       AuthWebClientProperties webClientProperties) {
        final var matcher = new HeaderWsMessageMatcher(properties.getTokenHeader());
        final var converter = new HeaderWsAuthenticationConverter(
                properties.getTokenHeader(),
                OneTimeTokenAuthenticationToken::unauthenticated
        );
        final var manager = getTokenAuthenticationManager(webClient, objectMapper, webClientProperties);
        return new WsAuthenticationInterceptor(matcher, converter, manager);
    }

    private @NotNull TokenAuthenticationManager getTokenAuthenticationManager(WebClient webClient,
                                                                              ObjectMapper objectMapper,
                                                                              AuthWebClientProperties webClientProperties) {
        final var authenticationService = new TokenRemoteAuthenticationService(
                webClient,
                objectMapper,
                webClientProperties.getAuthPath(),
                webClientProperties.getAuthResponseTimeout(),
                webClientProperties.getTokenHeader(),
                webClientProperties.getResponseUsernameField()
        );
        return new TokenAuthenticationManager(
                authenticationService,
                defaultAuthorities
        );
    }
}
