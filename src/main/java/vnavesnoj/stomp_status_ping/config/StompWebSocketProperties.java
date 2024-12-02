package vnavesnoj.stomp_status_ping.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
@ConfigurationProperties("app.websocket.stomp.server")
public class StompWebSocketProperties {

    String[] endpoints;
    String[] applicationDestinationPrefixes;
    String[] brokerDestinationPrefixes;
    String[] origins;
    String principalHeader;
}
