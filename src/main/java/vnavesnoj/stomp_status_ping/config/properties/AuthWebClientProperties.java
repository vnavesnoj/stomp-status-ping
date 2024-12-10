package vnavesnoj.stomp_status_ping.config.properties;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
@ConfigurationProperties("app.webclient.auth")
public class AuthWebClientProperties {

    String baseUrl;
    String authPath;
    int writeTimeout;
    int readTimeout;
    int connectTimeout;
    long authResponseTimeout;
    String tokenHeader;
}
