package vnavesnoj.stomp_status_ping.config.properties;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
@ConfigurationProperties("app.websocket.stomp.server.destination")
public class AppStompDestinationProperties {

    String prefix;

    String currentUserStatus;
}
