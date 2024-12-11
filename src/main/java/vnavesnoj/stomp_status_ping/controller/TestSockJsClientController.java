package vnavesnoj.stomp_status_ping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/test")
public class TestSockJsClientController {

    @RequestMapping ("/sock-js-client")
    public String sockJsClientLocalAuth() {
        return "sock-js-stomp-client";
    }
}
