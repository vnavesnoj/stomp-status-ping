package vnavesnoj.stomp_status_ping.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionDeletedDto;
import vnavesnoj.stomp_status_ping.service.ActiveWsSessionService;
import vnavesnoj.stomp_status_ping.websocket.payload.UserStatus;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class WebSocketDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    private final ActiveWsSessionService service;
    private final UserStatusNotifier notifier;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        final var username = event.getUser().getName();
        final var sessionId = event.getSessionId();
        log.info("Session %s disconnected with status %s".formatted(
                username + ":" + sessionId, event.getCloseStatus()
        ));
        final var maybeDeleted = service.deleteWithResponse(username, sessionId);
        if (maybeDeleted.isEmpty()) {
            sendToSubscribersOfflineStatus(username, event.getTimestamp());
        } else {
            maybeDeleted
                    .filter(ActiveWsSessionDeletedDto::isLast)
                    .map(ActiveWsSessionDeletedDto::getSession)
                    .ifPresent(item -> sendToSubscribersOfflineStatus(item.getUsername(), event.getTimestamp()));
        }
    }

    private void sendToSubscribersOfflineStatus(String username, Long timestamp) {
        notifier.sendToSubscribers(username, UserStatus.OFFLINE, timestamp);
    }
}
