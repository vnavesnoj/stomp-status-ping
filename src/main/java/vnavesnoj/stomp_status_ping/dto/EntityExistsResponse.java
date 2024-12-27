package vnavesnoj.stomp_status_ping.dto;

import lombok.Value;

import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
public class EntityExistsResponse {

    boolean exists;
    boolean single;
    Instant instant;
}
