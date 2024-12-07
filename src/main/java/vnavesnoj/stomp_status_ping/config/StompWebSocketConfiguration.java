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
import vnavesnoj.stomp_status_ping.data.ActiveWsUserSessionRepository;
import vnavesnoj.stomp_status_ping.interceptor.NoopChannelInterceptor;
import vnavesnoj.stomp_status_ping.websocket.WsSessionUpdateInterceptor;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(StompWebSocketProperties.class)
@EnableWebSocketMessageBroker
public class StompWebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final StompWebSocketProperties properties;
    private final ActiveWsUserSessionRepository repository;
    private final TaskScheduler messageBrokerTaskScheduler;

    public StompWebSocketConfiguration(StompWebSocketProperties properties,
                                       ActiveWsUserSessionRepository repository,
                                       @Lazy TaskScheduler messageBrokerTaskScheduler) {
        this.properties = properties;
        this.repository = repository;
        this.messageBrokerTaskScheduler = messageBrokerTaskScheduler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(properties.getEndpoints())
                .withSockJS()
                .setHeartbeatTime(properties.getHeartbeatTime());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(properties.getApplicationDestinationPrefixes())
                .enableSimpleBroker(properties.getBrokerDestinationPrefixes())
                .setHeartbeatValue(new long[]{10000L, 10000L})
                .setTaskScheduler(messageBrokerTaskScheduler);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WsSessionUpdateInterceptor(repository));
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
    }

    @Bean("csrfChannelInterceptor")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    ChannelInterceptor noopCsrfChannelInterceptor() {
        return new NoopChannelInterceptor();
    }
}
