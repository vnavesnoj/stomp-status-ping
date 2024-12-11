package vnavesnoj.stomp_status_ping.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import vnavesnoj.stomp_status_ping.dto.AuthenticationResult;
import vnavesnoj.stomp_status_ping.service.TokenAuthenticationService;

import java.util.List;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class TokenAuthenticationManager implements AuthenticationManager {

    private final TokenAuthenticationService service;
    private final List<GrantedAuthority> defaultGrantedAuthorities;

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        return Optional.of(authentication)
                .map(Authentication::getCredentials)
                .filter(item -> item.getClass().isAssignableFrom(String.class))
                .map(item -> (String) item)
                .map(service::getByToken)
                .filter(AuthenticationResult::isAuthenticated)
                .map(AuthenticationResult::getUsername)
                .map(item -> OneTimeTokenAuthenticationToken.authenticated(
                        item,
                        defaultGrantedAuthorities
                ))
                .orElseThrow(() -> new BadCredentialsException("Invalid token"));
    }
}
