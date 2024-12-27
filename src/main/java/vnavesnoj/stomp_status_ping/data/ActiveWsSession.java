package vnavesnoj.stomp_status_ping.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("session")
public class ActiveWsSession {

    @Id
    String id;

    @Indexed
    String username;

    Long connectionTime;

    Long lastAccessedTime;

    @TimeToLive
    Long ttl;
}
