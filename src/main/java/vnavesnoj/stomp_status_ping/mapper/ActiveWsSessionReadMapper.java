package vnavesnoj.stomp_status_ping.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vnavesnoj.stomp_status_ping.data.ActiveWsSession;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionReadDto;

import java.time.Duration;
import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Component
public class ActiveWsSessionReadMapper implements Mapper<ActiveWsSession, ActiveWsSessionReadDto> {

    @Override
    public ActiveWsSessionReadDto map(ActiveWsSession object) {
        return new ActiveWsSessionReadDto(
                object.getUsername(),
                object.getSessionId(),
                Instant.ofEpochSecond(object.getConnectionTime()),
                Instant.ofEpochSecond(object.getLastAccessedTime()),
                Duration.ofSeconds(object.getTtl())
        );
    }
}
