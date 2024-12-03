package vnavesnoj.stomp_status_ping.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Configuration
public class Initializer extends AbstractHttpSessionApplicationInitializer {

    public Initializer() {
        super(RedisSessionConfiguration.class);
    }
}
