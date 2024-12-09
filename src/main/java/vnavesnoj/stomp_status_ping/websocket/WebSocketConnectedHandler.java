package vnavesnoj.stomp_status_ping.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import vnavesnoj.stomp_status_ping.config.properties.BrokerDestinationProperties;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionCreateDto;
import vnavesnoj.stomp_status_ping.service.ActiveWsSessionService;
import vnavesnoj.stomp_status_ping.websocket.payload.UserStatus;
import vnavesnoj.stomp_status_ping.websocket.payload.UserStatusPayload;

import java.time.Instant;
import java.util.Map;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@Component
public class WebSocketConnectedHandler implements ApplicationListener<SessionConnectedEvent> {

    private final ActiveWsSessionService sessionService;
    private final SimpMessagingTemplate template;
    private final String userStatusBrokerDestination;

    public WebSocketConnectedHandler(ActiveWsSessionService sessionService,
                                     SimpMessagingTemplate template,
                                     BrokerDestinationProperties properties) {
        this.sessionService = sessionService;
        this.template = template;
        this.userStatusBrokerDestination = properties.getPrefix() + properties.getUserStatus();
    }


    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        final var username = event.getUser().getName();
        final var sessionId = SimpMessageHeaderAccessor.getSessionId(event.getMessage().getHeaders());
        log.info("New session %s connected by %s".formatted(sessionId, username));
        final var isFirstSession = sessionService.findAllByUsername(username).isEmpty();
        final var session = new ActiveWsSessionCreateDto(
                username,
                sessionId,
                Instant.ofEpochMilli(event.getTimestamp())
        );
        final var savedSession = sessionService.create(session);
        log.debug("New session saved: %s".formatted(savedSession));
        if (isFirstSession) {
            final Map<String, Object> headers = Map.of("subscription", username);
            final var payload = new UserStatusPayload(username, UserStatus.ONLINE);
            template.convertAndSend(userStatusBrokerDestination, payload, headers);
            log.info("Send status of the user %s to subscribers".formatted(username));
        }
    }
}
