package vnavesnoj.stomp_status_ping.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import vnavesnoj.stomp_status_ping.config.properties.ActiveWsSessionEntityProperties;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories(basePackages = "vnavesnoj.stomp_status_ping.data", enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
@EnableConfigurationProperties(ActiveWsSessionEntityProperties.class)
public class RedisConfiguration {
}
