package vnavesnoj.stomp_status_ping.service;

import vnavesnoj.stomp_status_ping.dto.AuthenticationResult;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface TokenAuthenticationService {

    AuthenticationResult getByToken(String token);
}
