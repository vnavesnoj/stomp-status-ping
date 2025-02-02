package vnavesnoj.stomp_status_ping.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vnavesnoj.stomp_status_ping.config.properties.AppStompDestinationProperties;
import vnavesnoj.stomp_status_ping.config.properties.BrokerDestinationProperties;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Controller
@ConditionalOnProperty(name = "app.testing-page.enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("/test")
public class TestStompJsClientController {

    private final String stompUrl;
    private final String brokerUserStatusDestination;
    private final String appUserStatusDestination;

    public TestStompJsClientController(@Value("${app.testing-page.stomp-url:null}") String stompUrl,
                                       BrokerDestinationProperties brokerProperties,
                                       AppStompDestinationProperties appProperties) {
        this.stompUrl = stompUrl;
        this.brokerUserStatusDestination = brokerProperties.getPrefix() + brokerProperties.getUserStatus();
        this.appUserStatusDestination = appProperties.getPrefix() + appProperties.getCurrentUserStatus();
    }


    @RequestMapping("/stomp-js-client")
    public String stompJsClient(Model model) {
        model.addAttribute("stompUrl", stompUrl);
        model.addAttribute("brokerUserStatusDestination", brokerUserStatusDestination);
        model.addAttribute("appUserStatusDestination", appUserStatusDestination);
        return "stomp-js-client";
    }

    @RequestMapping("/stomp-js-client-with-cookie")
    public String stompJsClientWithCookie(Model model,
                                          @RequestParam("cookie") String cookie,
                                          HttpServletResponse response) throws URISyntaxException {
        model.addAttribute("stompUrl", stompUrl);
        model.addAttribute("brokerUserStatusDestination", brokerUserStatusDestination);
        model.addAttribute("appUserStatusDestination", appUserStatusDestination);
        final var splited = cookie.split("=");
        if (splited.length == 2) {
            final var cookieObj = new Cookie(splited[0], splited[1]);
            cookieObj.setPath(new URI(stompUrl).getPath());
            cookieObj.setHttpOnly(true);
            response.addCookie(cookieObj);
        }
        return "stomp-js-client-with-cookie";
    }
}
