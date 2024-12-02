package vnavesnoj.stomp_status_ping.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(StompWebSocketProperties.class)
@EnableWebSocketMessageBroker
public class StompWebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final StompWebSocketProperties properties;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(properties.getEndpoints()).withSockJS();
//        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(properties.getApplicationDestinationPrefixes())
                .enableSimpleBroker(properties.getBrokerDestinationPrefixes());
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
    }
}
