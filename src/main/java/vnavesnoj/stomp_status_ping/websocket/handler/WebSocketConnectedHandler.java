package vnavesnoj.stomp_status_ping.websocket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionCreateDto;
import vnavesnoj.stomp_status_ping.service.ActiveWsSessionService;
import vnavesnoj.stomp_status_ping.websocket.UserStatusNotifier;
import vnavesnoj.stomp_status_ping.websocket.payload.UserStatus;

import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class WebSocketConnectedHandler implements ApplicationListener<SessionConnectedEvent> {

    private final ActiveWsSessionService sessionService;
    private final UserStatusNotifier notifier;


    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        final var username = event.getUser().getName();
        final var sessionId = SimpMessageHeaderAccessor.getSessionId(event.getMessage().getHeaders());
        log.info("New session %s connected by %s".formatted(sessionId, username));
        //TODO wih count() method
        final var isFirstSession = sessionService.findAllByUsername(username).isEmpty();
        final var session = new ActiveWsSessionCreateDto(
                username,
                sessionId,
                Instant.ofEpochMilli(event.getTimestamp())
        );
        final var savedSession = sessionService.create(session);
        log.debug("New session saved: %s".formatted(savedSession));
        if (isFirstSession) {
            notifier.sendToSubscribers(username, UserStatus.ONLINE, event.getTimestamp());
        }
    }
}
