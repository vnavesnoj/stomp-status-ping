package vnavesnoj.stomp_status_ping.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import vnavesnoj.stomp_status_ping.exception.InvalidSessionToUpdateException;
import vnavesnoj.stomp_status_ping.service.ActiveWsSessionService;

import java.util.Optional;

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
        if (messageType != CONNECT && messageType != CONNECT_ACK
                && messageType != DISCONNECT && messageType != DISCONNECT_ACK) {
            final var updatedSession = Optional.of(message)
                    .map(Message::getHeaders)
                    .map(SimpMessageHeaderAccessor::getSessionId)
                    .flatMap(service::updateLastAccessedTime)
                    .orElseThrow(
                            () -> log.throwing(
                                    new InvalidSessionToUpdateException("Could not update session last accessed time.")
                            )
                    );
            log.debug("Last accessed time updated. Session: {}", updatedSession.getSessionId());
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
