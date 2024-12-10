package vnavesnoj.stomp_status_ping.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import vnavesnoj.stomp_status_ping.config.properties.HttpCredentialProperties;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/test")
public class TestSockJsClientController {

    private final HttpCredentialProperties properties;

    @RequestMapping ("/sock-js-client/local-auth")
    public String sockJsClientLocalAuth(@Param("principal") String principal, HttpServletResponse response) {
        final var cookie = new Cookie(properties.getPrincipalCookie(), principal);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return "sock-js-stomp-client";
    }

    @RequestMapping ("/sock-js-client/remote-auth")
    public String sockJsClientRemoteAuth(@Param("token") String token, HttpServletResponse response) {
        final var cookie = new Cookie(properties.getTokenCookie(), token);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return "sock-js-stomp-client";
    }
}
