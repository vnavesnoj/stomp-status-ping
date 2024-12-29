package vnavesnoj.stomp_status_ping.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Getter
public class SessionExpiredEvent extends ApplicationEvent {

    private final String sessionId;

    public SessionExpiredEvent(Object source, String sessionId) {
        super(source);
        this.sessionId = sessionId;
    }
}
