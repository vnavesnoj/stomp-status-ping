package vnavesnoj.stomp_status_ping.websocket.interceptor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import vnavesnoj.stomp_status_ping.dto.SessionExpiredEvent;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.springframework.messaging.simp.stomp.DefaultStompSession.EMPTY_PAYLOAD;
import static org.springframework.messaging.support.MessageBuilder.createMessage;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Component
public class WsSessionRelevanceMatcherInterceptor implements ChannelInterceptor, ApplicationListener<SessionExpiredEvent> {

    private final Cache<String, ExpiredSession> expiredSessions = CacheBuilder.newBuilder()
            .concurrencyLevel(1)
            .expireAfterWrite(10L, TimeUnit.MINUTES)
            .build();
    public static final String ERROR_MESSAGE = "Session expired";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final var errorMessage = Optional.of(message)
                .map(Message::getHeaders)
                .map(SimpMessageHeaderAccessor::getSessionId)
                .map(expiredSessions::getIfPresent)
                .map(item -> {
                    expiredSessions.invalidate(item.getSessionId());
                    final var accessor = StompHeaderAccessor.create(StompCommand.ERROR);
                    accessor.setSessionId(item.getSessionId());
                    accessor.setMessage(ERROR_MESSAGE);
                    return createMessage(EMPTY_PAYLOAD, accessor.getMessageHeaders());
                });
        if (errorMessage.isPresent()) {
            return ChannelInterceptor.super.preSend(errorMessage.get(), channel);
        } else {
            return ChannelInterceptor.super.preSend(message, channel);
        }
    }

    @Override
    public void onApplicationEvent(SessionExpiredEvent event) {
        final var session = new ExpiredSession(
                event.getSessionId(),
                Instant.ofEpochMilli(event.getTimestamp())
        );
        expiredSessions.put(event.getSessionId(), session);
    }

    @Value
    private static class ExpiredSession {

        String sessionId;
        Instant expiredAd;
    }
}
