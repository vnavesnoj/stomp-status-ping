package vnavesnoj.stomp_status_ping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;
import vnavesnoj.stomp_status_ping.dto.SessionExpiredEvent;

import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class SessionExpiredHandler implements ApplicationListener<RedisKeyExpiredEvent<?>> {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void onApplicationEvent(@NotNull RedisKeyExpiredEvent event) {
        Optional.of(event)
                .map(item -> new SessionExpiredEvent(
                        this,
                        new String(event.getId())
                ))
                .ifPresent(eventPublisher::publishEvent);
    }

    @Override
    public boolean supportsAsyncExecution() {
        return false;
    }
}