package vnavesnoj.stomp_status_ping.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class CookieAuthenticationFilter implements Filter {

    private final RequestMatcher requestMatcher;
    private final AuthenticationConverter converter;
    private final AuthenticationManager authenticationManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Optional.of(request)
                .map(item -> (HttpServletRequest) item)
                .filter(requestMatcher::matches)
                .map(converter::convert)
                .map(authenticationManager::authenticate)
                .filter(Authentication::isAuthenticated)
                .ifPresent(authentication ->
                        SecurityContextHolder.getContext().setAuthentication(authentication));
        chain.doFilter(request, response);
    }
}
