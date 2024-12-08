package vnavesnoj.stomp_status_ping.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Data
@RedisHash("session")
public class ActiveWsSession {

    @Id
    String id;

    @Indexed
    String username;

    String sessionId;

    Long connectionTime;

    Long lastAccessedTime;

    @TimeToLive
    Long ttl;

    public final static String ID_DELIMITER = ".";

    private ActiveWsSession(String id, String username, String sessionId, Long connectionTime, Long lastAccessedTime, Long ttl) {
        this.id = id;
        this.username = username;
        this.sessionId = sessionId;
        this.connectionTime = connectionTime;
        this.lastAccessedTime = lastAccessedTime;
        this.ttl = ttl;
    }

    public static ActiveWsSession of(String username, String sessionId, Long connectionTime, Long lastAccessedTime, Long ttl) {
        return new ActiveWsSession(
                username + ID_DELIMITER + sessionId,
                username,
                sessionId,
                connectionTime,
                lastAccessedTime,
                ttl
        );
    }
}
