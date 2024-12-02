package vnavesnoj.stomp_status_ping.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import vnavesnoj.stomp_status_ping.config.StompWebSocketProperties;

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
        sockJsClient = new SockJsClient(transports);
    }

    @Test
    void test() throws ExecutionException, InterruptedException, URISyntaxException {
        final var httpHeaders = new WebSocketHttpHeaders();
        httpHeaders.add(properties.getPrincipalHeader(), "dummy1");
        final var uri = new URI("ws://localhost:" + this.port + properties.getEndpoints()[0]);
        CompletableFuture<WebSocketSession> wsSession = sockJsClient.execute(
                this.webSocketHandler,
                httpHeaders,
                uri
        );
        assertThat(wsSession.get().isOpen()).isTrue();
    }
}
