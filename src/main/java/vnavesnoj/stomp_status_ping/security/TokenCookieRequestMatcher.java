package vnavesnoj.stomp_status_ping.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class TokenCookieRequestMatcher implements RequestMatcher {

    private final String tokenCookie;

    @Override
    public boolean matches(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .anyMatch(cookie -> cookie.getName().equals(tokenCookie));
    }
}
