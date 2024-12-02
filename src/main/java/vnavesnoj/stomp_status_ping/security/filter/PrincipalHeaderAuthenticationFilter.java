package vnavesnoj.stomp_status_ping.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public class PrincipalHeaderAuthenticationFilter extends OncePerRequestFilter {

    private final String principalHeader;

    public PrincipalHeaderAuthenticationFilter(String principalHeader) {
        this.principalHeader = principalHeader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException {
        final var principal = request.getHeader(principalHeader);
        if (principal == null || principal.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect principal");
        } else {
            final var user = new User(
                    principal,
                    "",
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
            final var authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
    }
}
