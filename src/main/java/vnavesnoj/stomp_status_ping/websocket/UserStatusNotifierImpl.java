package vnavesnoj.stomp_status_ping.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import vnavesnoj.stomp_status_ping.config.properties.BrokerDestinationProperties;
import vnavesnoj.stomp_status_ping.websocket.payload.UserStatus;
import vnavesnoj.stomp_status_ping.websocket.payload.UserStatusPayload;

import java.util.Map;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@Component
public class UserStatusNotifierImpl implements UserStatusNotifier {

    private final SimpMessagingTemplate template;
    private final String userStatusBrokerDestination;

    public UserStatusNotifierImpl(SimpMessagingTemplate template,
                                  BrokerDestinationProperties properties) {
        this.template = template;
        this.userStatusBrokerDestination = properties.getPrefix() + properties.getUserStatus();
    }

    @Override
    public void sendToSubscribers(String username, UserStatus status, Long timestamp) {
        final Map<String, Object> headers = Map.of("subscription", username);
        final var payload = new UserStatusPayload(username, UserStatus.ONLINE, timestamp);
        template.convertAndSend(userStatusBrokerDestination, payload, headers);
        log.info("Send status of the user %s to subscribers".formatted(username));
    }
}
