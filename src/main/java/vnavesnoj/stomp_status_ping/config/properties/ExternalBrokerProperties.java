package vnavesnoj.stomp_status_ping.config.properties;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
@ConfigurationProperties(prefix = "app.websocket.stomp.broker.external")
public class ExternalBrokerProperties {

    boolean enabled;
    String virtualHost;
    String relayHost;
    int relayPort;
    String clientLogin;
    String clientPasscode;
    String systemLogin;
    String systemPasscode;
    long heartbeatSendInterval;
    long heartbeatReceiveInterval;
}
