package vnavesnoj.stomp_status_ping.controller;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.testcontainers.containers.GenericContainer;
import vnavesnoj.stomp_status_ping.config.TestRedisConfiguration;
import vnavesnoj.stomp_status_ping.config.properties.StompWebSocketProperties;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Import(TestRedisConfiguration.class)
@ImportTestcontainers(TestRedisConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WsConnectionTest {

    @Autowired
    private StompWebSocketProperties properties;
    @Value("${local.server.port}")
    private String port;
    @Autowired
    private WebSocketHandler webSocketHandler;
    private SockJsClient sockJsClient;


    @BeforeEach
    void init() {
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());
        new StompSessionHandler();
        sockJsClient = new SockJsClient(transports);
    }

    @Test
    void testConnection() throws ExecutionException, InterruptedException, URISyntaxException {
        CompletableFuture<WebSocketSession> wsSession = simpleConnection("dummy1");
        assertThat(wsSession.get().isOpen()).isTrue();
    }

    @Test
    void testRedisRepository() throws URISyntaxException, ExecutionException, InterruptedException {
        final var principalName = "dummy1";
        final var stompSession = stompConnection(principalName).get();
        assertThat(stompSession.isConnected()).isTrue();
//        final Map<String, ? extends Session> byPrincipalNameSessions = repository.findByPrincipalName(principalName);
//        assertThat(byPrincipalNameSessions).isNotEmpty();
    }

    @NotNull
    private CompletableFuture<WebSocketSession> simpleConnection(String principal) throws URISyntaxException {
        final var httpHeaders = new WebSocketHttpHeaders();
//        httpHeaders.add("Cookie", credentialProperties.getPrincipalCookie() + "=" + principal);
        final var uri = new URI("ws://localhost:" + this.port + properties.getEndpoints()[0]);
        return sockJsClient.execute(
                this.webSocketHandler,
                httpHeaders,
                uri
        );
    }

    @NotNull
    private CompletableFuture<StompSession> stompConnection(String principal) throws URISyntaxException {
        final var httpHeaders = new WebSocketHttpHeaders();
//        httpHeaders.add(properties.getPrincipalHeader(), principal);
        final var uri = new URI("ws://localhost:" + this.port + properties.getEndpoints()[0]);
        return new WebSocketStompClient(sockJsClient).connectAsync(uri.toString(), httpHeaders, new StompSessionHandler());
    }

    @TestConfiguration
    static class Config {

        @Bean
        GenericContainer redisContainer() {
            GenericContainer redisContainer = new GenericContainer("redis:7").withExposedPorts(6379);
            redisContainer.start();
            return redisContainer;
        }

        @Bean
        LettuceConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory(redisContainer().getHost(), redisContainer().getFirstMappedPort());
        }

    }
}
