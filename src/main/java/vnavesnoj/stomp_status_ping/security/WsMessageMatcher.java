/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.security;

import org.springframework.messaging.Message;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface WsMessageMatcher {

    boolean matches(Message<?> message);
}
