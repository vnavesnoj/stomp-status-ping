package vnavesnoj.stomp_status_ping.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import vnavesnoj.stomp_status_ping.config.properties.AuthWebClientProperties;
import vnavesnoj.stomp_status_ping.dto.AuthResponse;
import vnavesnoj.stomp_status_ping.dto.AuthenticationResult;

import java.time.Duration;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@Service
public class TokenRemoteAuthenticationService implements TokenAuthenticationService {

    private final WebClient authWebClient;
    private final String authPath;
    private final long authResponseTimeout;

    public TokenRemoteAuthenticationService(WebClient authWebClient,
                                            AuthWebClientProperties properties) {
        this.authWebClient = authWebClient;
        this.authPath = properties.getAuthPath();
        this.authResponseTimeout = properties.getAuthResponseTimeout();
    }


    @Override
    public AuthenticationResult getByToken(String token) {
        final var authResponse = authWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(authPath).build())
                .retrieve()
                .bodyToFlux(AuthResponse.class)
                //TODO optimize
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getStatusCode().is4xxClientError()
                                ? Mono.empty() : Mono.error(ex))
                .doOnError(log::error)
                .blockFirst(Duration.ofMillis(authResponseTimeout));
        return Optional.ofNullable(authResponse)
                .map(item -> new AuthenticationResult(true, item.getNickname()))
                .orElseGet(() -> new AuthenticationResult(false, null));
    }
}
