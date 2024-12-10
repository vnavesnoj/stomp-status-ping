/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.config;

import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vnavesnoj.stomp_status_ping.config.properties.CredentialProperties;
import vnavesnoj.stomp_status_ping.security.filter.PrincipalCookieAuthenticationFilter;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Configuration
@EnableConfigurationProperties(CredentialProperties.class)
@ConditionalOnProperty(name = "app.http.auth.remote", havingValue = "false")
public class LocalAuthenticationFilterConfiguration implements AuthenticationFilterConfiguration {

    private final String principalCookie;

    public LocalAuthenticationFilterConfiguration(CredentialProperties properties) {
        this.principalCookie = properties.getPrincipalCookie();
    }

    @Override
    @Bean
    public Filter authenticationFilter() {
        return new PrincipalCookieAuthenticationFilter(principalCookie);
    }
}
