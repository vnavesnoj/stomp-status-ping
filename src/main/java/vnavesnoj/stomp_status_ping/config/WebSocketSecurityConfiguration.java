package vnavesnoj.stomp_status_ping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import vnavesnoj.stomp_status_ping.config.properties.AppStompDestinationProperties;
import vnavesnoj.stomp_status_ping.config.properties.BrokerDestinationProperties;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfiguration {

    private final String brokerUserStatusDestination;
    private final String appUserStatusDestination;

    public WebSocketSecurityConfiguration(BrokerDestinationProperties brokerProperties,
                                          AppStompDestinationProperties appProperties) {
        this.brokerUserStatusDestination = brokerProperties.getPrefix() + brokerProperties.getUserStatus();
        this.appUserStatusDestination = appProperties.getPrefix() + appProperties.getCurrentUserStatus();
    }

    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages
                .anyMessage().authenticated()
                .simpSubscribeDestMatchers(brokerUserStatusDestination, appUserStatusDestination).permitAll()
                .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
                .anyMessage().denyAll();
        return messages.build();
    }
}