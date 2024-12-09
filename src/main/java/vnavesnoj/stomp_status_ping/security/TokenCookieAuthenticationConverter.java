package vnavesnoj.stomp_status_ping.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import vnavesnoj.stomp_status_ping.exception.TokenNotFoundException;

import java.util.Arrays;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class TokenCookieAuthenticationConverter implements AuthenticationConverter {

    private final String tokenCookie;

    @Override
    public Authentication convert(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(item -> item.getName().equals(tokenCookie))
                .map(Cookie::getValue)
                .map(OneTimeTokenAuthenticationToken::unauthenticated)
                .findFirst()
                .orElseThrow(TokenNotFoundException::new);
    }
}
