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

import java.security.Principal;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class WebSocketConnectedHandler implements ApplicationListener<SessionConnectedEvent> {

    private final ActiveWsSessionService service;
    private final UserStatusNotifier notifier;


    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        final var username = Optional.ofNullable(event.getUser())
                .map(Principal::getName)
                .orElseThrow(() -> log.throwing(new NullPointerException()));
        final var sessionId = SimpMessageHeaderAccessor.getSessionId(event.getMessage().getHeaders());
        log.info("New session {} connected by {}", sessionId, username);
        //TODO with count() method
        final var entityExistsResponse = service.existsByUsername(username);
        final var newSession = new ActiveWsSessionCreateDto(username, sessionId, entityExistsResponse.getInstant());
        final var createdSession = service.create(newSession);
        if (!entityExistsResponse.isExists()) {
            notifier.sendToSubscribers(
                    createdSession.getUsername(),
                    UserStatus.ONLINE,
                    entityExistsResponse.getInstant().toEpochMilli()
            );
        }
    }
}
