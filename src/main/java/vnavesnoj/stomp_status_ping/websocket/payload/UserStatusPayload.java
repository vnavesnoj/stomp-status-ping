package vnavesnoj.stomp_status_ping.websocket.payload;

import lombok.Value;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
public class UserStatusPayload {

    String username;

    UserStatus status;
}
