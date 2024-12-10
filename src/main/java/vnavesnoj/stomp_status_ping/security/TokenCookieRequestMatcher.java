package vnavesnoj.stomp_status_ping.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

import static java.util.function.Predicate.not;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class TokenCookieRequestMatcher implements RequestMatcher {

    private final String tokenCookie;

    @Override
    public boolean matches(HttpServletRequest request) {
        final var cookies = request.getCookies();
        return cookies != null && Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(tokenCookie))
                .map(Cookie::getValue)
                .findFirst()
                .filter(not(String::isBlank))
                .isPresent();
    }
}
