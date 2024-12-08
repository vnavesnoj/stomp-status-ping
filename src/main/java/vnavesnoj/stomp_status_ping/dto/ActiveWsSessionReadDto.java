package vnavesnoj.stomp_status_ping.dto;

import lombok.Value;

import java.time.Duration;
import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
public class ActiveWsSessionReadDto {

    String username;

    String sessionId;

    Instant connectionTime;

    Instant lastAccessedTime;

    Duration ttl;
}
