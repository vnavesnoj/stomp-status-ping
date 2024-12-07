package vnavesnoj.stomp_status_ping.data;

import lombok.Builder;
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
@Builder
public class ActiveWsUserSession {

    @Id
    String id;

    @Indexed
    String username;

    Long connectionTime;

    @TimeToLive
    Long ttl;
}
