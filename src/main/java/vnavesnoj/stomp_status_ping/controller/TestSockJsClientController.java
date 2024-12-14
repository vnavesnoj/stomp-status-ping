package vnavesnoj.stomp_status_ping.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import vnavesnoj.stomp_status_ping.config.properties.StompWebSocketProperties;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Controller
@ConditionalOnProperty(name = "app.testing-page.enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("/test")
public class TestSockJsClientController {

    private final String sockJsEndpoint;

    public TestSockJsClientController(StompWebSocketProperties wsProperties) {
        sockJsEndpoint = wsProperties.getEndpoints()[0];
    }


    @RequestMapping("/sock-js-client")
    public String sockJsClientLocalAuth(HttpServletRequest request,
                                        Model model) {
        final var sockJsUrl = "%s://%s:%d%s".formatted(
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                sockJsEndpoint
        );
        model.addAttribute("sockJsUrl", sockJsUrl);
        return "sock-js-stomp-client";
    }
}
