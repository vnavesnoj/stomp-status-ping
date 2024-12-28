package vnavesnoj.stomp_status_ping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;
import vnavesnoj.stomp_status_ping.data.ActiveWsSession;
import vnavesnoj.stomp_status_ping.dto.SessionExpiredEvent;

import java.time.Instant;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class SessionExpiredHandler implements ApplicationListener<RedisKeyExpiredEvent<ActiveWsSession>> {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void onApplicationEvent(RedisKeyExpiredEvent<ActiveWsSession> event) {
        Optional.of(event)
                .map(item -> new SessionExpiredEvent(
                        event.getSource(),
                        new String(event.getId()),
                        Instant.ofEpochMilli(event.getTimestamp())
                ))
                .ifPresent(eventPublisher::publishEvent);
    }
}