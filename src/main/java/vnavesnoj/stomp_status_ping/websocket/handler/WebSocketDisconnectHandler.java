package vnavesnoj.stomp_status_ping.websocket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import vnavesnoj.stomp_status_ping.service.ActiveWsSessionService;
import vnavesnoj.stomp_status_ping.websocket.handler.component.UserStatusNotifier;
import vnavesnoj.stomp_status_ping.websocket.payload.UserStatus;

import java.security.Principal;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class WebSocketDisconnectHandler implements ApplicationListener<SessionDisconnectEvent> {

    private final ActiveWsSessionService service;
    private final UserStatusNotifier notifier;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        final var sessionId = event.getSessionId();
        log.info("Session {} disconnected with status {}", sessionId, event.getCloseStatus());
        final var username = Optional.ofNullable(event.getUser())
                .map(Principal::getName)
                .orElse(null);
        if (username != null) {
            final var entityExistsResponse = service.existsByUsername(username);
            if (entityExistsResponse.isExists()) {
                service.delete(sessionId);
            }
            if (entityExistsResponse.isExists() && entityExistsResponse.isSingle()) {
                notifier.sendToSubscribers(username, UserStatus.OFFLINE, entityExistsResponse.getInstant().toEpochMilli());
            }
        }
    }
}
