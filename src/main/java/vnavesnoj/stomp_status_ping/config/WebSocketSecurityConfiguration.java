package vnavesnoj.stomp_status_ping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public class WebSocketSecurityConfiguration {

    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages.anyMessage().authenticated();
        return messages.build();
    }
}
