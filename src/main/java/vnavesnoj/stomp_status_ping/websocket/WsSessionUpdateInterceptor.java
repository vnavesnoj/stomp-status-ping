package vnavesnoj.stomp_status_ping.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import vnavesnoj.stomp_status_ping.service.ActiveWsSessionService;

import static org.springframework.messaging.simp.SimpMessageType.*;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class WsSessionUpdateInterceptor implements ChannelInterceptor {

    private final ActiveWsSessionService service;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final var messageType = SimpMessageHeaderAccessor.getMessageType(message.getHeaders());
        final var sessionId = SimpMessageHeaderAccessor.getSessionId(message.getHeaders());
        final var username = SimpMessageHeaderAccessor.getUser(message.getHeaders()).getName();
        log.debug("Receive a message with a type %s from %s with session = %s".formatted(messageType, username, sessionId));
        if (messageType == SUBSCRIBE || messageType == UNSUBSCRIBE || messageType == HEARTBEAT) {
            service.findByUsernameAndSessionId(username, sessionId)
                    .flatMap(item -> service.updateLastAccessedTime(username, sessionId))
                    .ifPresent(updated -> log.debug("Last accessed time updated. Session: %s".formatted(updated.getSessionId())));
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
