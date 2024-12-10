/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.security;

import org.springframework.messaging.Message;
import org.springframework.security.core.Authentication;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface WsAuthenticationConverter {

    Authentication convert(Message<?> message);
}
