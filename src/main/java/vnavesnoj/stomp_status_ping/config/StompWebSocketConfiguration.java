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
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import vnavesnoj.stomp_status_ping.config.properties.AppStompDestinationProperties;
import vnavesnoj.stomp_status_ping.config.properties.BrokerDestinationProperties;
import vnavesnoj.stomp_status_ping.config.properties.ExternalBrokerProperties;
import vnavesnoj.stomp_status_ping.config.properties.StompWebSocketProperties;
import vnavesnoj.stomp_status_ping.websocket.interceptor.NoopChannelInterceptor;
import vnavesnoj.stomp_status_ping.websocket.interceptor.WsAuthenticationInterceptor;
import vnavesnoj.stomp_status_ping.websocket.interceptor.WsSessionRelevanceMatcherInterceptor;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties({
        StompWebSocketProperties.class,
        BrokerDestinationProperties.class,
        AppStompDestinationProperties.class,
        ExternalBrokerProperties.class
})
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StompWebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final StompWebSocketProperties wsProperties;
    private final BrokerDestinationProperties brokerProperties;
    private final AppStompDestinationProperties appStompProperties;
    private final ExternalBrokerProperties externalBrokerProperties;
    private final ChannelInterceptor wsSessionUpdateInterceptor;
    private final WsAuthenticationInterceptor wsAuthenticationInterceptor;
    private final WsSessionRelevanceMatcherInterceptor relevanceMatcherInterceptor;
    private final TaskScheduler messageBrokerTaskScheduler;
    private final WsSessionRelevanceMatcherInterceptor wsSessionRelevanceMatcherInterceptor;

    public StompWebSocketConfiguration(StompWebSocketProperties wsProperties,
                                       BrokerDestinationProperties brokerProperties,
                                       AppStompDestinationProperties appStompProperties, ExternalBrokerProperties externalBrokerProperties,
                                       ChannelInterceptor wsSessionUpdateInterceptor,
                                       WsAuthenticationInterceptor wsAuthenticationInterceptor, WsSessionRelevanceMatcherInterceptor relevanceMatcherInterceptor,
                                       @Lazy TaskScheduler messageBrokerTaskScheduler, WsSessionRelevanceMatcherInterceptor wsSessionRelevanceMatcherInterceptor) {
        this.wsProperties = wsProperties;
        this.brokerProperties = brokerProperties;
        this.appStompProperties = appStompProperties;
        this.externalBrokerProperties = externalBrokerProperties;
        this.wsSessionUpdateInterceptor = wsSessionUpdateInterceptor;
        this.wsAuthenticationInterceptor = wsAuthenticationInterceptor;
        this.relevanceMatcherInterceptor = relevanceMatcherInterceptor;
        this.messageBrokerTaskScheduler = messageBrokerTaskScheduler;
        this.wsSessionRelevanceMatcherInterceptor = wsSessionRelevanceMatcherInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(wsProperties.getEndpoints())
                .setAllowedOrigins(wsProperties.getOrigins());
        registry.setErrorHandler(new StompSubProtocolErrorHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        if (externalBrokerProperties.isEnabled()) {
            final var stompBrokerRelayRegistration = registry.enableStompBrokerRelay(brokerProperties.getPrefix())
                    .setVirtualHost(externalBrokerProperties.getVirtualHost())
                    .setRelayHost(externalBrokerProperties.getRelayHost())
                    .setRelayPort(externalBrokerProperties.getRelayPort())
                    .setClientLogin(externalBrokerProperties.getClientLogin())
                    .setClientPasscode(externalBrokerProperties.getClientPasscode());
            if (externalBrokerProperties.getSystemLogin() != null) {
                stompBrokerRelayRegistration.setSystemLogin(externalBrokerProperties.getSystemLogin());
            }
            if (externalBrokerProperties.getSystemPasscode() != null) {
                stompBrokerRelayRegistration.setSystemPasscode(externalBrokerProperties.getSystemPasscode());
            }
            stompBrokerRelayRegistration.setTaskScheduler(messageBrokerTaskScheduler)
                    .setSystemHeartbeatSendInterval(externalBrokerProperties.getHeartbeatSendInterval())
                    .setSystemHeartbeatReceiveInterval(externalBrokerProperties.getHeartbeatReceiveInterval());
        } else {
            registry.enableSimpleBroker(brokerProperties.getPrefix())
                    .setHeartbeatValue(new long[]{wsProperties.getServerHeartbeat(), wsProperties.getClientHeartbeat()})
                    .setTaskScheduler(messageBrokerTaskScheduler);
        }
        registry.setApplicationDestinationPrefixes(appStompProperties.getPrefix(), brokerProperties.getPrefix());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(
                wsAuthenticationInterceptor,
                wsSessionRelevanceMatcherInterceptor,
                wsSessionUpdateInterceptor
        );
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(relevanceMatcherInterceptor);
        WebSocketMessageBrokerConfigurer.super.configureClientOutboundChannel(registration);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(ExceptionWebSocketHandlerDecorator::new);
        WebSocketMessageBrokerConfigurer.super.configureWebSocketTransport(registry);
    }

    @Bean("csrfChannelInterceptor")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    ChannelInterceptor noopCsrfChannelInterceptor() {
        return new NoopChannelInterceptor();
    }
}
