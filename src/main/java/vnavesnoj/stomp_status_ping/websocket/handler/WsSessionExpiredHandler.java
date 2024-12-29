package vnavesnoj.stomp_status_ping.websocket.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import vnavesnoj.stomp_status_ping.dto.SessionExpiredEvent;

import static org.springframework.messaging.simp.stomp.DefaultStompSession.EMPTY_PAYLOAD;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Component
public class WsSessionExpiredHandler implements ApplicationListener<SessionExpiredEvent> {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void onApplicationEvent(SessionExpiredEvent event) {

        eventPublisher.publishEvent(new SessionDisconnectEvent(
                this,
                createDisconnectMessage(event.getSessionId()),
                event.getSessionId(),
                new CloseStatus(1000, "Session expired")
        ));
    }

    private Message<byte[]> createDisconnectMessage(String sessionId) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
        headerAccessor.setSessionId(sessionId);
        return MessageBuilder.createMessage(EMPTY_PAYLOAD, headerAccessor.getMessageHeaders());
    }
}
