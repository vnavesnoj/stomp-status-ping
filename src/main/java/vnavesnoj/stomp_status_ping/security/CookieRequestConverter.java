package vnavesnoj.stomp_status_ping.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class CookieRequestConverter implements AuthenticationConverter {

    private final String cookieName;

    @Override
    public Authentication convert(HttpServletRequest request) {
        return Optional.of(request)
                .map(HttpServletRequest::getCookies)
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(item -> cookieName.equals(item.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                )
                .map(OneTimeTokenAuthenticationToken::unauthenticated)
                .orElse(null);
    }
}
