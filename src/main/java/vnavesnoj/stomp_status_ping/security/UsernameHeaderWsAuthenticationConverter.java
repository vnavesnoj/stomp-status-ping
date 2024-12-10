/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.security;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static java.util.function.Predicate.not;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class UsernameHeaderWsAuthenticationConverter implements WsAuthenticationConverter {

    private final String usernameHeader;

    @Override
    public Authentication convert(Message<?> message) {
        return Optional.of(message)
                .map(item -> StompHeaderAccessor.getFirstNativeHeader(usernameHeader, message.getHeaders()))
                .filter(not(String::isBlank))
                .map(item -> UsernamePasswordAuthenticationToken.unauthenticated(item, null))
                .orElse(null);
    }
}
