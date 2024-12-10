/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;
import vnavesnoj.stomp_status_ping.config.properties.AuthWebClientProperties;
import vnavesnoj.stomp_status_ping.config.properties.CredentialProperties;
import vnavesnoj.stomp_status_ping.security.TokenAuthenticationManager;
import vnavesnoj.stomp_status_ping.security.TokenCookieAuthenticationConverter;
import vnavesnoj.stomp_status_ping.security.TokenCookieRequestMatcher;
import vnavesnoj.stomp_status_ping.service.TokenRemoteAuthenticationService;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Configuration
@ConditionalOnMissingBean(LocalAuthenticationFilterConfiguration.class)
public class RemoteAuthenticationFilterConfiguration implements AuthenticationFilterConfiguration {

    private final WebClient webClient;
    private final AuthWebClientProperties webClientProperties;
    private final CredentialProperties credentialProperties;

    @Override
    @Bean
    public Filter authenticationFilter() {
        final var authenticationService = new TokenRemoteAuthenticationService(
                webClient,
                webClientProperties.getAuthPath(),
                webClientProperties.getAuthResponseTimeout(),
                webClientProperties.getTokenHeader()
        );

        final var authenticationFilter = new AuthenticationFilter(
                new TokenAuthenticationManager(authenticationService),
                new TokenCookieAuthenticationConverter(credentialProperties.getTokenCookie())
        );
        authenticationFilter.setRequestMatcher(new TokenCookieRequestMatcher(credentialProperties.getTokenCookie()));
        return authenticationFilter;
    }
}
