package vnavesnoj.stomp_status_ping.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import vnavesnoj.stomp_status_ping.config.properties.AppStompDestinationProperties;
import vnavesnoj.stomp_status_ping.config.properties.BrokerDestinationProperties;
import vnavesnoj.stomp_status_ping.config.properties.StompWebSocketProperties;
import vnavesnoj.stomp_status_ping.websocket.NoopChannelInterceptor;
import vnavesnoj.stomp_status_ping.websocket.WsAuthenticationInterceptor;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties({
        StompWebSocketProperties.class,
        BrokerDestinationProperties.class,
        AppStompDestinationProperties.class
})
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StompWebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final StompWebSocketProperties wsProperties;
    private final BrokerDestinationProperties brokerProperties;
    private final AppStompDestinationProperties appStompProperties;
    private final ChannelInterceptor wsSessionUpdateInterceptor;
    private final WsAuthenticationInterceptor wsAuthenticationInterceptor;
    private final TaskScheduler messageBrokerTaskScheduler;

    public StompWebSocketConfiguration(StompWebSocketProperties wsProperties,
                                       BrokerDestinationProperties brokerProperties,
                                       AppStompDestinationProperties appStompProperties,
                                       ChannelInterceptor wsSessionUpdateInterceptor,
                                       WsAuthenticationInterceptor wsAuthenticationInterceptor,
                                       @Lazy TaskScheduler messageBrokerTaskScheduler) {
        this.wsProperties = wsProperties;
        this.brokerProperties = brokerProperties;
        this.appStompProperties = appStompProperties;
        this.wsSessionUpdateInterceptor = wsSessionUpdateInterceptor;
        this.wsAuthenticationInterceptor = wsAuthenticationInterceptor;
        this.messageBrokerTaskScheduler = messageBrokerTaskScheduler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(wsProperties.getEndpoints())
                .withSockJS()
                .setHeartbeatTime(wsProperties.getServerHeartbeat());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(appStompProperties.getPrefix(), brokerProperties.getPrefix())
                .enableSimpleBroker(brokerProperties.getPrefix())
                .setHeartbeatValue(new long[]{wsProperties.getServerHeartbeat(), wsProperties.getClientHeartbeat()})
                .setTaskScheduler(messageBrokerTaskScheduler);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(wsAuthenticationInterceptor, wsSessionUpdateInterceptor);
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
    }

    @Bean("csrfChannelInterceptor")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    ChannelInterceptor noopCsrfChannelInterceptor() {
        return new NoopChannelInterceptor();
    }
}
