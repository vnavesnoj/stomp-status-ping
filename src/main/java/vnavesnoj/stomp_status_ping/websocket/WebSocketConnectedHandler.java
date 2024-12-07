package vnavesnoj.stomp_status_ping.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import vnavesnoj.stomp_status_ping.data.ActiveWsUserSession;
import vnavesnoj.stomp_status_ping.data.ActiveWsUserSessionRepository;
import vnavesnoj.stomp_status_ping.websocket.payload.UserStatus;
import vnavesnoj.stomp_status_ping.websocket.payload.UserStatusPayload;

import java.time.Instant;
import java.util.Map;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class WebSocketConnectedHandler implements ApplicationListener<SessionConnectedEvent> {

    private final ActiveWsUserSessionRepository repository;
    private final SimpMessagingTemplate template;

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        final var user = event.getUser().getName();
        log.info("New session connected by %s".formatted(event.getUser().getName()));
        if (repository.findAllByUsername(user).isEmpty()) {
            final Map<String, Object> headers = Map.of("subscription", user);
            final var payload = new UserStatusPayload(user, UserStatus.ONLINE);
            template.convertAndSend("/topic/status", payload, headers);
        }
        final var session = ActiveWsUserSession.of(
                event.getUser().getName(),
                SimpMessageHeaderAccessor.getSessionId(event.getMessage().getHeaders()),
                Instant.now().getEpochSecond(),
                Instant.now().getEpochSecond(),
                60L
        );
        repository.save(session);
        log.info("New session saved: %s".formatted(session));
    }

    @Override
    public boolean supportsAsyncExecution() {
        return false;
    }
}
