package vnavesnoj.stomp_status_ping.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vnavesnoj.stomp_status_ping.config.ActiveWsSessionEntityProperties;
import vnavesnoj.stomp_status_ping.data.ActiveWsSession;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionCreateDto;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Component
public class ActiveWsSessionCreateMapper implements Mapper<ActiveWsSessionCreateDto, ActiveWsSession> {

    private final ActiveWsSessionEntityProperties properties;

    @Override
    public ActiveWsSession map(ActiveWsSessionCreateDto object) {
        return ActiveWsSession.of(
                object.getUsername(),
                object.getSessionId(),
                object.getConnectionTime().getEpochSecond(),
                object.getConnectionTime().getEpochSecond(),
                properties.getTtl()
        );
    }
}
