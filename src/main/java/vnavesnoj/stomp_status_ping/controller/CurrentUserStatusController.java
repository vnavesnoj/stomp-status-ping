package vnavesnoj.stomp_status_ping.controller;

import lombok.NonNull;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import vnavesnoj.stomp_status_ping.config.properties.AppStompDestinationProperties;
import vnavesnoj.stomp_status_ping.config.properties.BrokerDestinationProperties;
import vnavesnoj.stomp_status_ping.service.ActiveWsSessionService;
import vnavesnoj.stomp_status_ping.websocket.payload.UserStatus;
import vnavesnoj.stomp_status_ping.websocket.payload.UserStatusPayload;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Controller
public class CurrentUserStatusController {

    private final String appUserStatusTopic;
    private final String brokerUserStatusTopic;
    private final ActiveWsSessionService service;

    public CurrentUserStatusController(AppStompDestinationProperties appStompProperties,
                                       BrokerDestinationProperties brokerProperties,
                                       ActiveWsSessionService service) {
        this.appUserStatusTopic = appStompProperties.getPrefix() + appStompProperties.getCurrentUserStatus();
        this.brokerUserStatusTopic = brokerProperties.getPrefix() + brokerProperties.getUserStatus();
        this.service = service;
    }


    @SubscribeMapping("/*")
    public Object handle(@Header("destination") String destination,
                         @Header("id") String id) {
        if (destination.equals(appUserStatusTopic) || destination.equals(brokerUserStatusTopic)) {
            return currentUserStatus(id);
        } else {
            return null;
        }
    }

    private UserStatusPayload currentUserStatus(@NonNull String username) {
        return Optional.of(username)
                .map(service::findAllByUsername)
                .filter(not(List::isEmpty))
                .map(item -> new UserStatusPayload(username, UserStatus.ONLINE, Instant.now().toEpochMilli()))
                .orElse(new UserStatusPayload(username, UserStatus.OFFLINE, Instant.now().toEpochMilli()));
    }
}
