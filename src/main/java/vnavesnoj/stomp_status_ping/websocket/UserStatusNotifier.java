package vnavesnoj.stomp_status_ping.websocket;

import vnavesnoj.stomp_status_ping.websocket.payload.UserStatus;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface UserStatusNotifier {

    void sendToSubscribers(String username, UserStatus status, Long timestamp);
}
