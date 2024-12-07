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
public class ActiveWsUserSession {

    @Id
    String id;

    @Indexed
    String username;

    Long connectionTime;

    Long lastAccessedTime;

    @TimeToLive
    Long ttl;

    private ActiveWsUserSession(String id, String username, Long connectionTime, Long lastAccessedTime, Long ttl) {
        this.id = id;
        this.username = username;
        this.connectionTime = connectionTime;
        this.lastAccessedTime = lastAccessedTime;
        this.ttl = ttl;
    }

    public static ActiveWsUserSession of(String username, String sessionId, Long connectionTime, Long lastAccessedTime, Long ttl) {
        return new ActiveWsUserSession(
                username + "." + sessionId,
                username,
                connectionTime,
                lastAccessedTime,
                ttl
        );
    }
}
