package vnavesnoj.stomp_status_ping.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import vnavesnoj.stomp_status_ping.dto.AuthenticationResult;

import java.time.Duration;
import java.util.Optional;

import static java.util.function.Predicate.not;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
public class TokenRemoteAuthenticationService implements TokenAuthenticationService {

    private final WebClient authWebClient;
    private final ObjectMapper objectMapper;
    private final String authPath;
    private final long authResponseTimeout;
    private final String tokenHeader;
    private final String usernameField;

    public TokenRemoteAuthenticationService(WebClient authWebClient,
                                            ObjectMapper objectMapper,
                                            String authPath,
                                            long authResponseTimeout,
                                            String tokenHeader,
                                            String usernameField) {
        this.authWebClient = authWebClient;
        this.objectMapper = objectMapper;
        this.authPath = authPath;
        this.authResponseTimeout = authResponseTimeout;
        this.tokenHeader = tokenHeader;
        this.usernameField = usernameField;
    }


    @Override
    public AuthenticationResult getByToken(String token) {
        //TODO create AuthWebClient class
        final var authResponse = authWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(authPath).build())
                .header(tokenHeader, token)
                .retrieve()
                .bodyToMono(String.class)
                //TODO optimize
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getStatusCode().is4xxClientError()
                                ? Mono.empty() : Mono.error(ex))
                .doOnError(log::error)
                .block(Duration.ofMillis(authResponseTimeout));
        return Optional.ofNullable(authResponse)
                .map(item -> {
                    try {
                        return objectMapper.readTree(item);
                    } catch (JsonProcessingException e) {
                        log.error("Cannot parse response from the authentication server. Response body must be in json format.", e);
                        return null;
                    }
                })
                .map(item -> {
                    final var node = item.at('/' + usernameField);
                    if (node.isMissingNode()) {
                        log.error("Cannot find field '{}' in response body from the authentication server.", usernameField);
                        return null;
                    }
                    return node.asText();
                })
                .filter(not(String::isBlank))
                .map(item -> new AuthenticationResult(true, item))
                .orElseGet(() -> new AuthenticationResult(false, null));
    }
}
