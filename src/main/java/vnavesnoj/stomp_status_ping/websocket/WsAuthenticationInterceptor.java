/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import vnavesnoj.stomp_status_ping.security.WsAuthenticationConverter;
import vnavesnoj.stomp_status_ping.security.WsMessageMatcher;

import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class WsAuthenticationInterceptor extends StompConnectInterceptor {

    private final WsMessageMatcher matcher;
    private final WsAuthenticationConverter converter;
    private final AuthenticationManager manager;

    @Override
    protected void doOnConnect(Message<?> message, MessageChannel channel) {
        Optional.of(message)
                .filter(matcher::matches)
                .map(converter::convert)
                .map(manager::authenticate)
                .filter(Authentication::isAuthenticated)
                .ifPresent(item -> {
                    final var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                    if (accessor != null) {
                        accessor.setUser(item);
                    }
                });
    }
}
