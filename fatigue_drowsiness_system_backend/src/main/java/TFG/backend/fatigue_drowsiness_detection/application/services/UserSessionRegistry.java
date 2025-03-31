package TFG.backend.fatigue_drowsiness_detection.application.services;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class UserSessionRegistry {

    private final Map<Integer, String> activeSessions = new ConcurrentHashMap<>();

    public void add(Integer userId) {
        activeSessions.put(userId, "active");
    }

    public void removeBySessionId(String sessionId) {
        activeSessions.values().remove(sessionId);
    }

    public boolean isActive(Integer userId) {
        return activeSessions.containsKey(userId);
    }

    public Set<Integer> getAllActiveUsers() {
        return activeSessions.keySet();
    }

    public void add(Integer userId, String sessionId) {
        activeSessions.put(userId, sessionId);
    }

}
