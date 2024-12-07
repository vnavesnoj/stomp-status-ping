package vnavesnoj.stomp_status_ping.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import vnavesnoj.stomp_status_ping.data.ActiveWsUserSession;
import vnavesnoj.stomp_status_ping.data.ActiveWsUserSessionRepository;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
public class WebSocketConnectHandler implements ApplicationListener<SessionConnectedEvent> {

    private final ActiveWsUserSessionRepository repository;

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        log.info("New session connected by %s".formatted(event.getUser().getName()));
        final var session = ActiveWsUserSession.builder()
                .username(event.getUser().getName())
                .connectionTime(event.getTimestamp())
                .build();
        repository.save(session);
        log.info("New session saved: %s".formatted(session));
        final var sessions = repository.findAllByUsername(session.getUsername());
        log.info("Total active sessions by user %s: %s".formatted(session.getUsername(), sessions.size()));
    }

    @Override
    public boolean supportsAsyncExecution() {
        return false;
    }
}
