package vnavesnoj.stomp_status_ping.websocket.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.SessionLimitExceededException;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import vnavesnoj.stomp_status_ping.websocket.interceptor.WsSessionRelevanceMatcherInterceptor;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
public class StompErrorHandler extends StompSubProtocolErrorHandler {

    private final String TARGET_ERROR_MESSAGE = WsSessionRelevanceMatcherInterceptor.ERROR_MESSAGE;

    @Override
    protected Message<byte[]> handleInternal(StompHeaderAccessor errorHeaderAccessor,
                                             byte[] errorPayload,
                                             Throwable cause,
                                             StompHeaderAccessor clientHeaderAccessor) {
        if (TARGET_ERROR_MESSAGE.equals(errorHeaderAccessor.getMessage())) {
            throw new SessionLimitExceededException(TARGET_ERROR_MESSAGE, CloseStatus.NORMAL);
        }
        return super.handleInternal(errorHeaderAccessor, errorPayload, cause, clientHeaderAccessor);
    }
}
