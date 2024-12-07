package vnavesnoj.stomp_status_ping.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.Session;
import vnavesnoj.stomp_status_ping.data.ActiveWsUserSessionRepository;
import vnavesnoj.stomp_status_ping.websocket.WebSocketConnectHandler;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Configuration
public class WebSocketHandlersConfiguration<S extends Session> {

    private final ActiveWsUserSessionRepository sessionRepository;

    @Bean
    public WebSocketConnectHandler webSocketConnectHandler() {
        return new WebSocketConnectHandler(sessionRepository);
    }
}
