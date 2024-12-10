/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.security;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class HeaderWsMessageMatcher implements WsMessageMatcher {

    private final String header;

    @Override
    public boolean matches(Message<?> message) {
        return Optional.of(message.getHeaders())
                .map(item -> StompHeaderAccessor.getFirstNativeHeader(header, item))
                .map(item -> !item.isBlank())
                .orElse(false);
    }
}
