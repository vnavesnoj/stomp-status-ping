package vnavesnoj.stomp_status_ping.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class CookieAuthenticationFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher;
    private final AuthenticationConverter converter;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional.of(request)
                .filter(requestMatcher::matches)
                .map(converter::convert)
                .map(authenticationManager::authenticate)
                .filter(Authentication::isAuthenticated)
                .ifPresent(authentication ->
                        SecurityContextHolder.getContext().setAuthentication(authentication));
        filterChain.doFilter(request, response);
    }
}
