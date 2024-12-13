/*
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

package vnavesnoj.stomp_status_ping.service;

import lombok.SneakyThrows;
import org.springframework.core.io.ResourceLoader;
import vnavesnoj.stomp_status_ping.dto.AuthenticationResult;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public class TokenPropertiesAuthenticationService implements TokenAuthenticationService {

    private final Properties tokens;

    @SneakyThrows
    public TokenPropertiesAuthenticationService(String tokenPropertiesPath,
                                                ResourceLoader resourceLoader) {
        this.tokens = Optional.of(tokenPropertiesPath)
                .map(resourceLoader::getResource)
                .map(item -> {
                    try {
                        return item.getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(item -> {
                            final var properties = new Properties();
                            try {
                                properties.load(item);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return properties;
                        }
                )
                .filter(Predicate.not(Properties::isEmpty))
                .orElseThrow(() -> new IllegalArgumentException(tokenPropertiesPath + " is empty"));
    }

    @SneakyThrows
    @Override
    public AuthenticationResult getByToken(String token) {
        return Optional.of(tokens)
                .map(item -> item.getProperty(token))
                .map(item -> new AuthenticationResult(true, item))
                .orElseGet(() -> new AuthenticationResult(false, null));
    }
}
