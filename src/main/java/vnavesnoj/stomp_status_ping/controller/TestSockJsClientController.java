package vnavesnoj.stomp_status_ping.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import vnavesnoj.stomp_status_ping.config.StompWebSocketProperties;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/test")
public class TestSockJsClientController {

    private final StompWebSocketProperties properties;

    @RequestMapping ("/sock-js-client")
    public String sockJsClient(@Param("principal") String principal, HttpServletResponse response) {
        final var cookie = new Cookie(properties.getPrincipalCookie(), principal);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return "sock-js-stomp-client";
    }
}
