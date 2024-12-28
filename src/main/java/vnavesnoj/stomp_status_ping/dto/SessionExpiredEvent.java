package vnavesnoj.stomp_status_ping.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Getter
public class SessionExpiredEvent extends ApplicationEvent {

    private final String sessionId;
    private final Instant timestamp;

    public SessionExpiredEvent(Object source, String sessionId, Instant timestamp) {
        super(source);
        this.sessionId = sessionId;
        this.timestamp = timestamp;
    }

    public SessionExpiredEvent(Object source, String sessionId) {
        this(source, sessionId, Instant.now());
    }
}
