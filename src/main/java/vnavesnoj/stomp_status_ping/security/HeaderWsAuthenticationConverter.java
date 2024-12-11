/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.security;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.function.Function;

import static java.util.function.Predicate.not;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class HeaderWsAuthenticationConverter implements WsAuthenticationConverter {

    private final String header;
    private final Function<? super String, ? extends Authentication> authenticationExtractor;

    @Override
    public Authentication convert(Message<?> message) {
        return Optional.of(message)
                .map(item -> StompHeaderAccessor.getFirstNativeHeader(header, message.getHeaders()))
                .filter(not(String::isBlank))
                .map(authenticationExtractor)
                .orElse(null);
    }
}
