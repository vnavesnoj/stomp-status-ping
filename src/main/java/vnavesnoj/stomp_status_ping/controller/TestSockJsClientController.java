package vnavesnoj.stomp_status_ping.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
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

    private final String sockJsUrl;

    public TestSockJsClientController(@Value("${app.testing-page.server-host}")
                                      String serverHost,
                                      StompWebSocketProperties wsProperties,
                                      ServerProperties serverProperties) {
        sockJsUrl = String.format("http://%s:%d%s",
                serverHost,
                serverProperties.getPort(),
                wsProperties.getEndpoints()[0]);
    }


    @RequestMapping("/sock-js-client")
    public String sockJsClientLocalAuth(Model model) {
        model.addAttribute("sockJsUrl", sockJsUrl);
        return "sock-js-stomp-client";
    }
}
