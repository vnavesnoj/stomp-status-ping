package vnavesnoj.stomp_status_ping.dto;

import lombok.Value;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
public class AuthenticationResult {

    boolean isAuthenticated;

    String username;
}
