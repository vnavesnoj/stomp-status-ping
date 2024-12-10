package vnavesnoj.stomp_status_ping.dto;

import lombok.Value;
import org.springframework.lang.Nullable;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
public class AuthenticationResult {

    boolean isAuthenticated;

    @Nullable
    String username;
}
