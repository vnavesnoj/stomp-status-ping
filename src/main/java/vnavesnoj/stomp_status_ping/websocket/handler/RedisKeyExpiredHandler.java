package vnavesnoj.stomp_status_ping.websocket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class RedisKeyExpiredHandler implements ApplicationListener<RedisKeyExpiredEvent<?>> {

    @Override
    public void onApplicationEvent(RedisKeyExpiredEvent<?> event) {
        //TODO close WebSocketSession
    }
}
