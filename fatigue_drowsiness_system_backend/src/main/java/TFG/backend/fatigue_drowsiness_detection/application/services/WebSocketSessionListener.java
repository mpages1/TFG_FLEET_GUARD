package TFG.backend.fatigue_drowsiness_detection.application.services;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketSessionListener {

    private final UserSessionRegistry sessionRegistry;

    public WebSocketSessionListener(UserSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userIdStr = accessor.getFirstNativeHeader("userId");
        String sessionId = accessor.getSessionId();

        if (userIdStr != null && sessionId != null) {
            Integer userId = Integer.parseInt(userIdStr);
            sessionRegistry.add(userId, sessionId);
            System.out.println("WebSocket CONNECT per userId: " + userId + " amb sessionId: " + sessionId);
        }
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        sessionRegistry.removeBySessionId(sessionId);
        System.out.println("WebSocket DISCONNECT amb sessionId: " + sessionId);
    }

    @MessageMapping("/register")
    public void registerUser(@Payload Map<String, Integer> payload) {
        Integer userId = payload.get("userId");
        sessionRegistry.add(userId);
    }

}
