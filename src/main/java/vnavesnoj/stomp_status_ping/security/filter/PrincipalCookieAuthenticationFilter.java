package vnavesnoj.stomp_status_ping.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static java.util.function.Predicate.not;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public class PrincipalCookieAuthenticationFilter extends OncePerRequestFilter {

    private final String principalCookie;

    public PrincipalCookieAuthenticationFilter(String principalCookie) {
        this.principalCookie = principalCookie;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException {
        final var cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            Arrays.stream(cookies)
                    .filter(item -> item.getName().equals(principalCookie))
                    .map(Cookie::getValue)
                    .findFirst()
                    .filter(not(String::isBlank))
                    .ifPresent(principal -> {
                        final var user = new User(
                                principal,
                                "",
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                        final var authentication =
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        }
        filterChain.doFilter(request, response);
    }
}
