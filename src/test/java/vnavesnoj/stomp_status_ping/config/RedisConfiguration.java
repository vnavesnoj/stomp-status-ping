package vnavesnoj.stomp_status_ping.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@TestConfiguration(proxyBeanMethods = false)
public class RedisConfiguration {

    @Bean
    @ServiceConnection(name = "redis")
    public GenericContainer<?> redisContainer() {
        return new GenericContainer<>("redis:7");
    }
}
