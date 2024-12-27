package vnavesnoj.stomp_status_ping.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
@AllArgsConstructor
public class ActiveWsSessionCreateDto {

    String username;

    String sessionId;

    Instant connectionTime;

    public ActiveWsSessionCreateDto(String username, String sessionId) {
        this.username = username;
        this.sessionId = sessionId;
        this.connectionTime = Instant.now();
    }
}
