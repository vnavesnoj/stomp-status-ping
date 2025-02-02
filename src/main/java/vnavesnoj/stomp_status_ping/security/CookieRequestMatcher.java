package vnavesnoj.stomp_status_ping.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class CookieRequestMatcher implements RequestMatcher {

    @NonNull
    private final String cookieName;

    @Override
    public boolean matches(HttpServletRequest request) {
        return Optional.of(request)
                .map(HttpServletRequest::getCookies)
                .filter(cookies -> Arrays.stream(cookies)
                        .map(Cookie::getName)
                        .anyMatch(cookieName::equals)
                )
                .isPresent();
    }
}
