package vnavesnoj.stomp_status_ping.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import vnavesnoj.stomp_status_ping.config.properties.AuthWebClientProperties;
import vnavesnoj.stomp_status_ping.service.TokenRemoteAuthenticationService;

import java.util.concurrent.TimeUnit;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@EnableConfigurationProperties(AuthWebClientProperties.class)
@Configuration
@ConditionalOnBean(TokenRemoteAuthenticationService.class)
public class AuthWebClientConfiguration {

    private final String apiUrl;
    private final int writeTimeout;
    private final int readTimeout;
    private final int connectTimeout;

    public AuthWebClientConfiguration(AuthWebClientProperties properties) {
        this.apiUrl = properties.getBaseUrl();
        this.readTimeout = properties.getReadTimeout();
        this.writeTimeout = properties.getWriteTimeout();
        this.connectTimeout = properties.getConnectTimeout();
    }

    @Bean
    public WebClient authWebClient(WebClient.Builder builder) {
        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS))
                );
        return builder
                .baseUrl(apiUrl)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create(tcpClient.configuration().connectionProvider())
                ))
                .build();
    }


}
