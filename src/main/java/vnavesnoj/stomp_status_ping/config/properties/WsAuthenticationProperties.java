/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.config.properties;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
@ConfigurationProperties("app.websocket.stomp.auth")
public class WsAuthenticationProperties {

    String usernameHeader;
    String tokenHeader;
    String tokenCookie;
    boolean local;
}
