package vnavesnoj.stomp_status_ping.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import vnavesnoj.stomp_status_ping.data.ActiveWsSessionRepository;

import java.time.Instant;

import static org.springframework.messaging.simp.SimpMessageType.*;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class WsSessionUpdateInterceptor implements ChannelInterceptor {

    private final ActiveWsSessionRepository repository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final var messageType = SimpMessageHeaderAccessor.getMessageType(message.getHeaders());
        if (messageType == SUBSCRIBE || messageType == UNSUBSCRIBE || messageType == HEARTBEAT) {
            final var sessionId = SimpMessageHeaderAccessor.getSessionId(message.getHeaders());
            final var username = SimpMessageHeaderAccessor.getUser(message.getHeaders()).getName();
            repository.findById(username + "." + sessionId)
                    .ifPresent(item -> {
                        item.setLastAccessedTime(Instant.now().getEpochSecond());
                        item.setTtl(60L);
                        repository.save(item);
                    });
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
