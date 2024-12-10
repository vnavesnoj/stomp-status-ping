package vnavesnoj.stomp_status_ping.mapper;

import org.springframework.stereotype.Component;
import vnavesnoj.stomp_status_ping.config.properties.ActiveWsSessionEntityProperties;
import vnavesnoj.stomp_status_ping.data.ActiveWsSession;
import vnavesnoj.stomp_status_ping.dto.ActiveWsSessionCreateDto;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Component
public class ActiveWsSessionCreateMapper implements Mapper<ActiveWsSessionCreateDto, ActiveWsSession> {

    private final Long entityTtl;

    public ActiveWsSessionCreateMapper(ActiveWsSessionEntityProperties properties) {
        this.entityTtl = properties.getTtl();
    }

    @Override
    public ActiveWsSession map(ActiveWsSessionCreateDto object) {
        return ActiveWsSession.of(
                object.getUsername(),
                object.getSessionId(),
                object.getConnectionTime().getEpochSecond(),
                object.getConnectionTime().getEpochSecond(),
                entityTtl
        );
    }
}
